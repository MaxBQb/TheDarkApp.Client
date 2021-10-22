package lab.maxb.dark.Presentation.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import lab.maxb.dark.Domain.Extra.Delegates.Once
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.Domain.Operations.solve
import lab.maxb.dark.Presentation.Repository.Interfaces.RecognitionTasksRepository


class SolveRecognitionTaskViewModel(
    private val recognitionTasksRepository: RecognitionTasksRepository,
) : ViewModel() {
    var id: String by Once()
    val recognitionTask: LiveData<RecognitionTask?> by lazy {
        recognitionTasksRepository.getRecognitionTask(id)
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