package lab.maxb.dark.Presentation.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import lab.maxb.dark.Domain.Operations.createRecognitionTask
import lab.maxb.dark.Presentation.Repository.Interfaces.RecognitionTasksRepository


class AddRecognitionTaskViewModel(
    private val recognitionTasksRepository: RecognitionTasksRepository,
) : ViewModel() {
    var imageUris: List<String> = mutableListOf()
    var names: List<String> = mutableListOf()

    fun addRecognitionTask(): Boolean {
        val task = createRecognitionTask(names, imageUris, null) ?: return false
        viewModelScope.launch {
            try {
                recognitionTasksRepository.addRecognitionTask(task)
            } catch (exc: Throwable) {
                Log.e("AddRecognitionTask", exc.localizedMessage ?: "")
            }
        }
        return true
    }
}