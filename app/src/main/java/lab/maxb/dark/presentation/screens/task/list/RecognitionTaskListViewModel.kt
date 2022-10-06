package lab.maxb.dark.presentation.screens.task.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapLatest
import lab.maxb.dark.domain.model.isUser
import lab.maxb.dark.domain.repository.RecognitionTasksRepository
import lab.maxb.dark.domain.usecase.profile.GetProfileUseCase
import lab.maxb.dark.presentation.extra.launch
import lab.maxb.dark.presentation.extra.stateIn
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class RecognitionTaskListViewModel(
    private val recognitionTasksRepository: RecognitionTasksRepository,
    getProfileUseCase: GetProfileUseCase
) : ViewModel() {
    private val profile = getProfileUseCase().stateIn(null)

    init {
        launch {
            profile.buffer()
        }
    }

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

    fun getImage(path: String) = recognitionTasksRepository.getRecognitionTaskImage(path)
}