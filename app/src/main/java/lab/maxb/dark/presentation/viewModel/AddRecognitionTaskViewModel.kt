package lab.maxb.dark.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import lab.maxb.dark.domain.operations.createRecognitionTask
import lab.maxb.dark.presentation.repository.interfaces.ProfileRepository
import lab.maxb.dark.presentation.repository.interfaces.RecognitionTasksRepository
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