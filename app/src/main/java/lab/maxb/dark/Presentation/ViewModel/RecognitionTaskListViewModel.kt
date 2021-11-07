package lab.maxb.dark.Presentation.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.Domain.Model.Server.Profile
import lab.maxb.dark.Domain.Model.isUser
import lab.maxb.dark.Presentation.Repository.Interfaces.RecognitionTasksRepository

class RecognitionTaskListViewModel(
    private val recognitionTasksRepository: RecognitionTasksRepository,
) : ViewModel() {
    fun getRecognitionTaskList(profile: Profile): LiveData<List<RecognitionTask>?>
        = if (profile.role.isUser())
            recognitionTasksRepository.getAllRecognitionTasksByReview(true)
        else
            recognitionTasksRepository.getAllRecognitionTasks()

    fun isTaskCreationAllowed(profile: Profile) = profile.role.isUser()
}