package lab.maxb.dark.Presentation.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.Presentation.Repository.Repository

class RecognitionTaskListViewModel : ViewModel() {
    val recognitionTaskList: LiveData<List<RecognitionTask>?>
        get() = Repository.recognitionTasks.getAllRecognitionTasks()
}