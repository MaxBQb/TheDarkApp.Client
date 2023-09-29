package lab.maxb.dark.ui.screens.add_task

import android.net.Uri
import kotlinx.coroutines.flow.asStateFlow
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.domain.usecase.task.CreateRecognitionTaskUseCase
import lab.maxb.dark.domain.usecase.task.GetTaskNameSynonymsUseCase
import lab.maxb.dark.ui.core.R
import lab.maxb.dark.ui.extra.FirstOnly
import lab.maxb.dark.ui.extra.ItemHolder
import lab.maxb.dark.ui.extra.LatestOnly
import lab.maxb.dark.ui.extra.launch
import lab.maxb.dark.ui.extra.map
import lab.maxb.dark.ui.extra.throwIfCancellation
import lab.maxb.dark.ui.extra.uiTextOf
import lab.maxb.dark.ui.screens.core.BaseViewModel
import org.koin.android.annotation.KoinViewModel
import kotlin.math.max
import lab.maxb.dark.ui.screens.add_task.AddTaskUiContract as Ui


@KoinViewModel
class AddRecognitionTaskViewModel(
    private val getTaskNameSynonymsUseCase: GetTaskNameSynonymsUseCase,
    private val createRecognitionTaskUseCase: CreateRecognitionTaskUseCase,
) : BaseViewModel<Ui.State, Ui.Event, Ui.SideEffect>() {
    private var suggestionsRequest by LatestOnly()
    private var addTaskRequest by FirstOnly()

    override fun handleEvent(event: Ui.Event): Unit = with(event) {
        when (this) {
            is Ui.Event.NameChanged -> setTexts(answer)
            is Ui.Event.ImagesAdded -> addImages(images)
            is Ui.Event.ImageChanged -> updateImage(position, image)
            is Ui.Event.ImageRemoved -> deleteImage(position)
            Ui.Event.Submit -> createRecognitionTask()
            is Ui.Event.EffectConsumed -> handleEffectConsumption(this)
        }
    }

    override fun getInitialState() = Ui.State()
    override val uiState = _uiState.asStateFlow()

    private fun createRecognitionTask() {
        addTaskRequest = launch {
            try {
                val state = uiState.value
                withLoading {
                    createRecognitionTaskUseCase(
                        state.names.map { it.value.trim() },
                        state.images.map { it.toString() },
                    )
                }
                setEffect { Ui.SideEffect.SubmitSuccess }
            } catch (e: Throwable) {
                e.throwIfCancellation()
                e.printStackTrace()
                setEffect {
                    Ui.SideEffect.UserMessage(
                        uiTextOf(R.string.addTask_message_notEnoughDataProvided)
                    )
                }
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

    private fun Ui.State.withImages(images: List<Uri>) = copy(
        images = images,
        allowedImageCount = max(RecognitionTask.MAX_IMAGES_COUNT - images.size, 0),
    )
}
