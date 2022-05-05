package lab.maxb.dark.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import lab.maxb.dark.Domain.Model.isUser
import lab.maxb.dark.presentation.Repository.Interfaces.ProfileRepository
import lab.maxb.dark.presentation.Repository.Interfaces.RecognitionTasksRepository
import lab.maxb.dark.presentation.view_model.utils.stateIn
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class RecognitionTaskListViewModel(
    private val recognitionTasksRepository: RecognitionTasksRepository,
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    private val profile = profileRepository.profileState

    @OptIn(ExperimentalCoroutinesApi::class)
    val recognitionTaskList = profile.flatMapLatest {
        if (it?.role?.isUser() ?: return@flatMapLatest flowOf(null))
            recognitionTasksRepository.getAllRecognitionTasksByReview(true).asFlow()
        else
            recognitionTasksRepository.getAllRecognitionTasks().asFlow()
    }.stateIn(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val isTaskCreationAllowed = profile.mapLatest {
        it?.role?.isUser() ?: false
    }.stateIn(false)
}