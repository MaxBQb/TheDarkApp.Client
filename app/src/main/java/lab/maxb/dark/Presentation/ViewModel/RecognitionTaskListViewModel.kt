package lab.maxb.dark.Presentation.ViewModel

import lab.maxb.dark.Presentation.Repository.Repository.Companion.getRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import lab.maxb.dark.Domain.Model.RecognitionTask

class RecognitionTaskListViewModel : ViewModel() {
    val recognitionTaskList: LiveData<List<RecognitionTask>?>
        get() = getRepository().getAllRecognitionTasks()

    fun removeRecognitionTask(task: RecognitionTask) {
        viewModelScope.launch {
            getRepository().deleteRecognitionTask(task)
        }
    }
}