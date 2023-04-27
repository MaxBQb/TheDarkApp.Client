package lab.maxb.dark.presentation.screens.task.add

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import lab.maxb.dark.R
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.domain.usecase.task.CreateRecognitionTaskUseCase
import lab.maxb.dark.domain.usecase.task.GetTaskNameSynonymsUseCase
import lab.maxb.dark.presentation.extra.*
import lab.maxb.dark.presentation.screens.core.BaseViewModel
import org.koin.android.annotation.KoinViewModel
import kotlin.math.max


@KoinViewModel
class AddRecognitionTaskViewModel(
    private val getTaskNameSynonymsUseCase: GetTaskNameSynonymsUseCase,
    private val createRecognitionTaskUseCase: CreateRecognitionTaskUseCase,
) : BaseViewModel<AddTaskUiState, AddTaskUiEvent>, ViewModel() {
    private var suggestionsRequest by LatestOnly()
    private var addTaskRequest by FirstOnly()

    override fun onEvent(event: AddTaskUiEvent): Unit = with(event) {
        when (this) {
            is AddTaskUiEvent.NameChanged -> setTexts(answer)
            is AddTaskUiEvent.ImagesAdded -> addImages(images)
            is AddTaskUiEvent.ImageChanged -> updateImage(position, image)
            is AddTaskUiEvent.ImageRemoved -> deleteImage(position)
            AddTaskUiEvent.Submit -> createRecognitionTask()
            AddTaskUiEvent.SubmitSuccess -> _uiState.update { it.copy(submitSuccess = null) }
            is AddTaskUiEvent.UserMessage -> _uiState.update {
                it.copy(userMessages = it.userMessages - this)
            }
        }
    }

    private val _uiState = MutableStateFlow(AddTaskUiState())
    override val uiState = _uiState.asStateFlow()

    private fun createRecognitionTask() {
        addTaskRequest = launch {
            try {
                val state = uiState.value
                _uiState.update { it.copy(isLoading = true) }
                createRecognitionTaskUseCase(
                    state.names.map { it.value },
                    state.images.map { it.toString() },
                )
                _uiState.update { it.copy(submitSuccess = AddTaskUiEvent.SubmitSuccess) }
            } catch (e: Throwable) {
                e.throwIfCancellation()
                e.printStackTrace()
                _uiState.update {
                    it.copy(
                        userMessages = it.userMessages + AddTaskUiEvent.UserMessage(
                            uiTextOf(R.string.addTask_message_notEnoughDataProvided)
                        )
                    )
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun setTexts(text: ItemHolder<String>) = _uiState.update { state ->
        val names = getInputList(state.names, text.map { it.trim() })
        requestSuggestions(names.map { it.value })
        state.copy(names = names)
    }

    private fun getInputList(
        list: List<ItemHolder<String>>,
        value: ItemHolder<String>,
    ) = list.map {
        if (it.id == value.id)
            value
        else
            it
    }.let {
        it.filterIndexed { pos, element ->
            element.value.isNotBlank() || pos == it.lastIndex
        }
    }.let {
        if (it.lastOrNull()?.value?.isBlank() != true)
            it + ItemHolder("")
        else it
    }

    private fun requestSuggestions(names: List<String>) {
        suggestionsRequest = launch {
            val suggestions = getTaskNameSynonymsUseCase(names)
            _uiState.update {
                it.copy(suggestions = suggestions.toList())
            }
        }
    }

    private fun deleteImage(position: Int) = _uiState.update { state ->
        state.withImages(
            state.images.filterIndexed { pos, _ -> position != pos }
        )
    }

    private fun addImages(uris: List<Uri>) = _uiState.update { state ->
        state.withImages(
            state.images + uris.take(state.allowedImageCount)
        )
    }

    private fun updateImage(position: Int, uri: Uri) = _uiState.update { state ->
        state.withImages(
            state.images.mapIndexed { pos, it ->
                if (position == pos) uri
                else it
            }
        )
    }

    private fun AddTaskUiState.withImages(images: List<Uri>) = copy(
        images = images,
        allowedImageCount = max(RecognitionTask.MAX_IMAGES_COUNT - images.size, 0),
    )
}
