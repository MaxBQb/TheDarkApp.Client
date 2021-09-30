package lab.maxb.dark.Presentation.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import lab.maxb.dark.Domain.Extra.Delegates.Once
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.Domain.Operations.solve
import lab.maxb.dark.Presentation.Repository.Repository


class SolveRecognitionTaskViewModel(application: Application) : AndroidViewModel(application) {
    var id: String by Once()
    val recognitionTask: LiveData<RecognitionTask?> by lazy {
        Repository.recognitionTasks.getRecognitionTask(id)
    }

    fun solveRecognitionTask(name: String): Boolean {
        val task = recognitionTask.value ?: return false
        return task.solve(name).also { isSolution ->
            if (isSolution)
                viewModelScope.launch {
                    Repository.recognitionTasks.deleteRecognitionTask(task)
                }
        }
    }
}