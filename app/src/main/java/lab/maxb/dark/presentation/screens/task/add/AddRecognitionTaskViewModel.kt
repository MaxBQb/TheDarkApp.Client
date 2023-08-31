package lab.maxb.dark.presentation.screens.task.add

import android.net.Uri
import kotlinx.coroutines.flow.asStateFlow
import lab.maxb.dark.R
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.domain.usecase.task.CreateRecognitionTaskUseCase
import lab.maxb.dark.domain.usecase.task.GetTaskNameSynonymsUseCase
import lab.maxb.dark.presentation.extra.FirstOnly
import lab.maxb.dark.presentation.extra.ItemHolder
import lab.maxb.dark.presentation.extra.LatestOnly
import lab.maxb.dark.presentation.extra.launch
import lab.maxb.dark.presentation.extra.map
import lab.maxb.dark.presentation.extra.throwIfCancellation
import lab.maxb.dark.presentation.extra.uiTextOf
import lab.maxb.dark.presentation.screens.core.BaseViewModel
import lab.maxb.dark.presentation.screens.core.effects.withEffectTriggered
import org.koin.android.annotation.KoinViewModel
import kotlin.math.max


@KoinViewModel
class AddRecognitionTaskViewModel(
    private val getTaskNameSynonymsUseCase: GetTaskNameSynonymsUseCase,
    private val createRecognitionTaskUseCase: CreateRecognitionTaskUseCase,
) : BaseViewModel<AddTaskUiState, AddTaskUiEvent, AddTaskUiSideEffect>() {
    private var suggestionsRequest by LatestOnly()
    private var addTaskRequest by FirstOnly()

    override fun handleEvent(event: AddTaskUiEvent): Unit = with(event) {
        when (this) {
            is AddTaskUiEvent.NameChanged -> setTexts(answer)
            is AddTaskUiEvent.ImagesAdded -> addImages(images)
            is AddTaskUiEvent.ImageChanged -> updateImage(position, image)
            is AddTaskUiEvent.ImageRemoved -> deleteImage(position)
            AddTaskUiEvent.Submit -> createRecognitionTask()
            is AddTaskUiEvent.EffectConsumed -> handleEffectConsumption(this)
        }
    }

    override fun getInitialState() = AddTaskUiState()
    override val uiState = _uiState.asStateFlow()

    private fun createRecognitionTask() {
        addTaskRequest = launch {
            try {
                val state = uiState.value
                setState { it.copy(isLoading = true) }
                createRecognitionTaskUseCase(
                    state.names.map { it.value.trim() },
                    state.images.map { it.toString() },
                )
                setState { it.withEffectTriggered(AddTaskUiSideEffect.SubmitSuccess) }
            } catch (e: Throwable) {
                e.throwIfCancellation()
                e.printStackTrace()
                setState {
                    it.withEffectTriggered(
                        AddTaskUiSideEffect.UserMessage(
                            uiTextOf(R.string.addTask_message_notEnoughDataProvided)
                        )
                    )
                }
            } finally {
                setState { it.copy(isLoading = false) }
            }
        }
    }

    private fun setTexts(text: ItemHolder<String>) = setState { state ->
        val names = getInputList(state.names, text.map { it.trimStart() })
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
            setState {
                it.copy(suggestions = suggestions.toList())
            }
        }
    }

    private fun deleteImage(position: Int) = setState { state ->
        state.withImages(
            state.images.filterIndexed { pos, _ -> position != pos }
        )
    }

    private fun addImages(uris: List<Uri>) = setState { state ->
        state.withImages(
            state.images + uris.take(state.allowedImageCount)
        )
    }

    private fun updateImage(position: Int, uri: Uri) = setState { state ->
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
