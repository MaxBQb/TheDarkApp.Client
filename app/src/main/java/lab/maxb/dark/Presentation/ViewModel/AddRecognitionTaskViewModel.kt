package lab.maxb.dark.Presentation.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import lab.maxb.dark.Domain.Operations.createRecognitionTask
import lab.maxb.dark.Presentation.Extra.SessionHolder
import lab.maxb.dark.Presentation.Repository.Interfaces.ProfileRepository
import lab.maxb.dark.Presentation.Repository.Interfaces.RecognitionTasksRepository
import lab.maxb.dark.Presentation.Repository.Interfaces.UsersRepository


class AddRecognitionTaskViewModel(
    private val recognitionTasksRepository: RecognitionTasksRepository,
    private val usersRepository: UsersRepository,
    private val profileRepository: ProfileRepository,
    private val sessionHolder: SessionHolder,
) : ViewModel() {
    var imageUris: List<String> = mutableListOf()
    var names: List<String> = mutableListOf()

    fun addRecognitionTask() = liveData(viewModelScope.coroutineContext) {
        try {
            val id = sessionHolder.session!!.profile!!.id
            val response = usersRepository.getUserOnce(id)
            val user = response ?: sessionHolder.session!!.profile!!
            if (response == null)
                usersRepository.addUser(user)
            val task = createRecognitionTask(names, imageUris, user)!!
            recognitionTasksRepository.addRecognitionTask(task)
            emit(true)
        } catch (exc: Throwable) {
            Log.e("AddRecognitionTask", exc.localizedMessage ?: "")
            emit(false)
        }
    }
}