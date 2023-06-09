package lab.maxb.dark.presentation.screens.task.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.mapLatest
import lab.maxb.dark.domain.model.isUser
import lab.maxb.dark.domain.usecase.profile.GetProfileUseCase
import lab.maxb.dark.domain.usecase.task.GetFavoriteRecognitionTaskListUseCase
import lab.maxb.dark.domain.usecase.task.GetRecognitionTaskImageUseCase
import lab.maxb.dark.domain.usecase.task.GetRecognitionTaskListUseCase
import lab.maxb.dark.domain.usecase.task.MarkFavoriteRecognitionTaskUseCase
import lab.maxb.dark.presentation.extra.launch
import lab.maxb.dark.presentation.extra.stateIn
import lab.maxb.dark.presentation.model.toPresentation
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class RecognitionTaskListViewModel(
    private val getRecognitionTaskImageUseCase: GetRecognitionTaskImageUseCase,
    private val markFavoriteRecognitionTaskUseCase: MarkFavoriteRecognitionTaskUseCase,
    getRecognitionTaskListUseCase: GetRecognitionTaskListUseCase,
    getFavoriteRecognitionTaskListUseCase: GetFavoriteRecognitionTaskListUseCase,
    getProfileUseCase: GetProfileUseCase,
) : ViewModel() {
    private val profile = getProfileUseCase().stateIn(null)

    init {
        launch {
            profile.buffer()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val recognitionTaskList = getRecognitionTaskListUseCase()
        .mapLatest { page -> page.map {
           it.toPresentation()
        } }.cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val favoriteRecognitionTaskList = getFavoriteRecognitionTaskListUseCase.getPaged()
        .mapLatest { page -> page.map {
           it.toPresentation()
        } }.cachedIn(viewModelScope)

    val hasFavorites = getFavoriteRecognitionTaskListUseCase.canBeObtained().stateIn(true)

    @OptIn(ExperimentalCoroutinesApi::class)
    val isTaskCreationAllowed = profile.mapLatest {
        it?.role?.isUser ?: false
    }.stateIn(false)

    fun getImage(path: String) = getRecognitionTaskImageUseCase(path)

    fun markFavorite(id: String, isFavorite: Boolean) = launch {
        markFavoriteRecognitionTaskUseCase(
            taskId = id,
            isFavorite = isFavorite,
        )
    }
}