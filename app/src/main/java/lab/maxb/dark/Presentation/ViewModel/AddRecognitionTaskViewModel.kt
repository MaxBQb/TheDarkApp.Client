package lab.maxb.dark.Presentation.ViewModel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import lab.maxb.dark.Domain.Operations.createRecognitionTask
import lab.maxb.dark.Presentation.Repository.Repository


class AddRecognitionTaskViewModel(application: Application) : AndroidViewModel(application) {
    var imageUri: Uri? = null

    fun addRecognitionTask(names: List<String>): Boolean {
        val task = createRecognitionTask(names, (imageUri ?: return false).toString(), null) ?: return false
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