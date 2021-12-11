package lab.maxb.dark.Presentation.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.Domain.Model.Role
import lab.maxb.dark.Domain.Operations.solve
import lab.maxb.dark.Presentation.Repository.Interfaces.ProfileRepository
import lab.maxb.dark.Presentation.Repository.Interfaces.RecognitionTasksRepository
import kotlin.properties.Delegates


class SolveRecognitionTaskViewModel(
    private val recognitionTasksRepository: RecognitionTasksRepository,
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    var id: String by Delegates.notNull()
    val recognitionTask: LiveData<RecognitionTask?> by lazy {
        recognitionTasksRepository.getRecognitionTask(id)
    }

    fun isReviewMode() = when (profileRepository.profile!!.role) {
        Role.MODERATOR, Role.ADMINISTRATOR -> true
        else -> false
    }

    fun markReviewed() = viewModelScope.launch {
        val task = recognitionTask.value ?: return@launch
        task.reviewed = true
        recognitionTasksRepository.updateRecognitionTask(task)
    }

    fun deleteTask() = viewModelScope.launch {
        val task = recognitionTask.value ?: return@launch
        recognitionTasksRepository.deleteRecognitionTask(task)
    }

    fun solveRecognitionTask(name: String): Boolean {
        val task = recognitionTask.value ?: return false
        return task.solve(name).also { isSolution ->
            if (isSolution)
                viewModelScope.launch {
                    recognitionTasksRepository.deleteRecognitionTask(task)
                }
        }
    }
}