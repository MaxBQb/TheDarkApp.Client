package lab.maxb.dark.presentation.screens.task.add

import android.net.Uri
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.presentation.extra.ItemHolder
import lab.maxb.dark.presentation.extra.UiText
import lab.maxb.dark.presentation.extra.UiTrigger
import lab.maxb.dark.presentation.extra.UiTriggers
import lab.maxb.dark.presentation.screens.core.UiEvent
import lab.maxb.dark.presentation.screens.core.UiState

data class AddTaskUiState(
    val names: List<ItemHolder<String>> = listOf(ItemHolder("")),
    val images: List<Uri> = emptyList(),
    val suggestions: List<String> = emptyList(),
    val userMessages: UiTriggers<AddTaskUiEvent.UserMessage> = UiTriggers(),
    val submitSuccess: AddTaskUiEvent.SubmitSuccess? = null,
    val allowedImageCount: Int = RecognitionTask.MAX_IMAGES_COUNT,
    val isLoading: Boolean = false,
) : UiState

sealed interface AddTaskUiEvent : UiEvent {
    data class NameChanged(val answer: ItemHolder<String>) : AddTaskUiEvent
    data class ImageChanged(val position: Int, val image: Uri) : AddTaskUiEvent
    data class ImageRemoved(val position: Int) : AddTaskUiEvent
    data class ImagesAdded(val images: List<Uri>) : AddTaskUiEvent
    object Submit : AddTaskUiEvent

    // UiTriggers
    data class UserMessage(val message: UiText) : UiTrigger(), AddTaskUiEvent
    object SubmitSuccess : UiTrigger(), AddTaskUiEvent
}