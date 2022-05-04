package lab.maxb.dark.Presentation.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import lab.maxb.dark.Domain.Operations.createRecognitionTask
import lab.maxb.dark.Presentation.Repository.Interfaces.ProfileRepository
import lab.maxb.dark.Presentation.Repository.Interfaces.RecognitionTasksRepository
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class AddRecognitionTaskViewModel(
    private val recognitionTasksRepository: RecognitionTasksRepository,
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    var imageUris: List<String> = mutableListOf()
    var names: List<String> = mutableListOf()

    fun addRecognitionTask() = liveData(viewModelScope.coroutineContext) {
        try {
            val user = profileRepository.profile.first()!!.user!!
            val task = createRecognitionTask(names, imageUris, user)!!
            recognitionTasksRepository.addRecognitionTask(task)
            emit(true)
        } catch (exc: Throwable) {
            Log.e("AddRecognitionTask", exc.localizedMessage ?: "")
            emit(false)
        }
    }
}