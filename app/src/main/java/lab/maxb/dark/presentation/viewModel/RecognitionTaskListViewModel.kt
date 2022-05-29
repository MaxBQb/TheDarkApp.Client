package lab.maxb.dark.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapLatest
import lab.maxb.dark.domain.model.isUser
import lab.maxb.dark.presentation.repository.interfaces.ImagesRepository
import lab.maxb.dark.presentation.repository.interfaces.ProfileRepository
import lab.maxb.dark.presentation.repository.interfaces.RecognitionTasksRepository
import lab.maxb.dark.presentation.viewModel.utils.stateIn
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class RecognitionTaskListViewModel(
    recognitionTasksRepository: RecognitionTasksRepository,
    profileRepository: ProfileRepository,
    private val imagesRepository: ImagesRepository,
) : ViewModel() {
    private val profile = profileRepository.profileState

    @OptIn(ExperimentalCoroutinesApi::class)
    val recognitionTaskList = recognitionTasksRepository
        .getAllRecognitionTasks().mapLatest { page ->
            page.filter {
                it.owner?.id != profile.firstOrNull()?.user?.id
            }
        }.cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val isTaskCreationAllowed = profile.mapLatest {
        it?.role?.isUser ?: false
    }.stateIn(false)

    fun getImage(path: String) = imagesRepository.getUri(path)
}