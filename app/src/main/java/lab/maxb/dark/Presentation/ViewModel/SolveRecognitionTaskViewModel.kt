package lab.maxb.dark.Presentation.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import lab.maxb.dark.Domain.Model.Profile
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.Domain.Model.Role
import lab.maxb.dark.Domain.Operations.solve
import lab.maxb.dark.Presentation.Repository.Interfaces.ProfileRepository
import lab.maxb.dark.Presentation.Repository.Interfaces.RecognitionTasksRepository
import kotlin.properties.Delegates


abstract class SolveRecognitionTaskViewModel : ViewModel() {
    var id: String by Delegates.notNull()
    abstract val recognitionTask: LiveData<RecognitionTask?>
    abstract fun isReviewMode(): Boolean
    abstract fun mark(isAllowed: Boolean): Job
    abstract fun solveRecognitionTask(name: String): Boolean
}

class SolveRecognitionTaskViewModelImpl(
    private val recognitionTasksRepository: RecognitionTasksRepository,
    private val profileRepository: ProfileRepository,
) : SolveRecognitionTaskViewModel() {
    override val recognitionTask by lazy  {
        recognitionTasksRepository.getRecognitionTask(id)
    }
    private var profile: Profile

    init {
        runBlocking {
            profile = profileRepository.getProfile().first()!!
        }
    }

    override fun isReviewMode() = when (profile.role) {
        Role.MODERATOR, Role.ADMINISTRATOR -> true
        else -> false
    }

    override fun mark(isAllowed: Boolean) = viewModelScope.launch {
        val task = recognitionTask.value ?: return@launch
        task.reviewed = isAllowed
        recognitionTasksRepository.markRecognitionTask(task)
    }

    override fun solveRecognitionTask(name: String): Boolean {
        val task = recognitionTask.value ?: return false
        return task.solve(name).also { isSolution ->
            if (isSolution)
                viewModelScope.launch {
                    recognitionTasksRepository.deleteRecognitionTask(task)
                }
        }
    }
}