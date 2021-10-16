package lab.maxb.dark.Presentation.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import lab.maxb.dark.Domain.Operations.createRecognitionTask
import lab.maxb.dark.Presentation.Repository.Repository


class AddRecognitionTaskViewModel(application: Application) : AndroidViewModel(application) {
    var imageUris: List<String> = mutableListOf()
    var names: List<String> = mutableListOf()

    fun addRecognitionTask(): Boolean {
        val task = createRecognitionTask(names, imageUris, null) ?: return false
        viewModelScope.launch {
            try {
                Repository.recognitionTasks.addRecognitionTask(task)
            } catch (exc: Throwable) {
                Log.e("AddRecognitionTask", exc.localizedMessage ?: "")
            }
        }
        return true
    }
}