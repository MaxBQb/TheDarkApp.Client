package lab.maxb.dark.presentation.viewModel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import lab.maxb.dark.R
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.domain.operations.createRecognitionTask
import lab.maxb.dark.presentation.extra.*
import lab.maxb.dark.presentation.repository.interfaces.ProfileRepository
import lab.maxb.dark.presentation.repository.interfaces.RecognitionTasksRepository
import lab.maxb.dark.presentation.repository.interfaces.SynonymsRepository
import lab.maxb.dark.presentation.viewModel.utils.ItemHolder
import lab.maxb.dark.presentation.viewModel.utils.LatestOnly
import lab.maxb.dark.presentation.viewModel.utils.firstNotNull
import lab.maxb.dark.presentation.viewModel.utils.map
import org.koin.android.annotation.KoinViewModel
import kotlin.math.max

data class AddTaskUiState(
    val names: List<ItemHolder<String>> = listOf(ItemHolder("")),
    val images: List<Uri> = emptyList(),
    val suggestions: List<String> = emptyList(),
    val userMessages: UiTriggers<AddTaskUiEvent.UserMessage> = UiTriggers(),
    val submitSuccess: AddTaskUiEvent.SubmitSuccess? = null,
    val allowedImageCount: Int = RecognitionTask.MAX_IMAGES_COUNT,
)


sealed interface AddTaskUiEvent {
    data class NameChanged(val answer: ItemHolder<String>) : AddTaskUiEvent
    data class ImageChanged(val position: Int, val image: Uri) : AddTaskUiEvent
    data class ImageRemoved(val position: Int) : AddTaskUiEvent
    data class ImagesAdded(val images: List<Uri>) : AddTaskUiEvent
    object Submit : AddTaskUiEvent

    // UiTriggers
    data class UserMessage(val message: UiText) : UiTrigger(), AddTaskUiEvent
    object SubmitSuccess : UiTrigger(), AddTaskUiEvent
}

@KoinViewModel
class AddRecognitionTaskViewModel(
    private val recognitionTasksRepository: RecognitionTasksRepository,
    profileRepository: ProfileRepository,
    private val synonymsRepository: SynonymsRepository,
    application: Application
) : AndroidViewModel(application) {
    private var suggestionsRequest by LatestOnly()
    private var addTaskRequest by LatestOnly()
    private val profile = profileRepository.profileState

    fun onEvent(event: AddTaskUiEvent): Unit = with(event) {
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
    val uiState = _uiState.asStateFlow()

    private fun createRecognitionTask() {
        addTaskRequest = launch {
            try {
                val user = profile.firstNotNull().user!!
                val state = uiState.value
                val task = createRecognitionTask(
                    state.names.map { it.value }.filter { it.isNotBlank() },
                    state.images.map { it.toString() },
                    user
                )!!
                recognitionTasksRepository.addRecognitionTask(task)
                _uiState.update { it.copy(submitSuccess = AddTaskUiEvent.SubmitSuccess) }
            } catch (e: Throwable) {
                e.printStackTrace()
                _uiState.update {
                    it.copy(
                        userMessages = it.userMessages + AddTaskUiEvent.UserMessage(
                            uiTextOf(R.string.addTask_message_notEnoughDataProvided)
                        )
                    )
                }
            }
        }
    }

    private fun setTexts(text: ItemHolder<String>) {
        _uiState.update { state ->
            val names = getInputList(state.names, text.map { it.trim() })
            requestSuggestions(names.map { it.value })
            state.copy(names = names)
        }
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
            val suggestions = synonymsRepository.getSynonyms(names.toSet())
                .map { it.trim() }
                .filterNot { it.isEmpty() }
                .toSet()
            _uiState.update {
                it.copy(suggestions = suggestions.toList())
            }
        }
    }

    fun deleteImage(position: Int) {
        _uiState.update { state ->
            state.withImages(
                state.images.filterIndexed { pos, _ -> position != pos }
            )
        }
    }

    fun addImages(uris: List<Uri>) {
        _uiState.update { state ->
            state.withImages(
                state.images + uris.take(state.allowedImageCount)
            )
        }
    }

    private fun updateImage(position: Int, uri: Uri) {
        _uiState.update { state ->
            state.withImages(
                state.images.mapIndexed { pos, it ->
                    if (position == pos) uri
                    else it
                }
            )
        }
    }

    private fun AddTaskUiState.withImages(images: List<Uri>) = copy(
        images = images,
        allowedImageCount = max(RecognitionTask.MAX_IMAGES_COUNT - images.size, 0),
    )
}

