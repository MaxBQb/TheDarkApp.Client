package lab.maxb.dark.Presentation.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.Presentation.Extra.SessionHolder
import lab.maxb.dark.Presentation.Repository.Interfaces.RecognitionTasksRepository
import lab.maxb.dark.Presentation.Repository.Interfaces.SessionRepository

class RecognitionTaskListViewModel(
    private val recognitionTasksRepository: RecognitionTasksRepository,
    private val sessionRepository: SessionRepository,
    private val sessionHolder: SessionHolder,
) : ViewModel() {
    val recognitionTaskList: LiveData<List<RecognitionTask>?>
        get() = recognitionTasksRepository.getAllRecognitionTasks()

    fun signOut() {
        viewModelScope.launch {
            sessionRepository.deleteSession(sessionHolder.session ?: return@launch)
            sessionHolder.session = null
        }
    }
}