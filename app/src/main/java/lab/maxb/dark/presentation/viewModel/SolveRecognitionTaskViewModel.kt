package lab.maxb.dark.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.domain.model.Role
import lab.maxb.dark.domain.operations.solve
import lab.maxb.dark.presentation.extra.launch
import lab.maxb.dark.presentation.repository.interfaces.ProfileRepository
import lab.maxb.dark.presentation.repository.interfaces.RecognitionTasksRepository
import lab.maxb.dark.presentation.viewModel.utils.stateIn
import org.koin.android.annotation.KoinViewModel
import kotlin.properties.Delegates


abstract class SolveRecognitionTaskViewModel : ViewModel() {
    var id: String by Delegates.notNull()
    abstract val recognitionTask: LiveData<RecognitionTask?>
    abstract val isReviewMode: StateFlow<Boolean>
    abstract fun mark(isAllowed: Boolean): Job
    abstract fun solveRecognitionTask(name: String): Boolean
}

@KoinViewModel
class SolveRecognitionTaskViewModelImpl(
    private val recognitionTasksRepository: RecognitionTasksRepository,
    profileRepository: ProfileRepository,
) : SolveRecognitionTaskViewModel() {
    override val recognitionTask get()
        = recognitionTasksRepository.getRecognitionTask(id)

    private val profile = profileRepository.profileState

    @OptIn(ExperimentalCoroutinesApi::class)
    override val isReviewMode get() = profile.mapLatest {
        when (it?.role) {
            Role.MODERATOR, Role.ADMINISTRATOR -> true
            else -> false
        }
    }.stateIn(false)

    override fun mark(isAllowed: Boolean) = launch {
        val task = recognitionTask.value ?: return@launch
        task.reviewed = isAllowed
        recognitionTasksRepository.markRecognitionTask(task)
    }

    override fun solveRecognitionTask(name: String): Boolean {
        val task = recognitionTask.value ?: return false
        return task.solve(name).also { isSolution ->
            if (isSolution) launch {
                recognitionTasksRepository.deleteRecognitionTask(task)
            }
        }
    }
}