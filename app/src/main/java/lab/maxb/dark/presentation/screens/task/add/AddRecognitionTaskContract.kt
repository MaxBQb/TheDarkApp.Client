package lab.maxb.dark.presentation.screens.task.add

import android.net.Uri
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.presentation.extra.ItemHolder
import lab.maxb.dark.presentation.extra.UiText
import lab.maxb.dark.presentation.screens.core.UiEvent
import lab.maxb.dark.presentation.screens.core.effects.EffectKey
import lab.maxb.dark.presentation.screens.core.effects.EmptyEffectsHolder
import lab.maxb.dark.presentation.screens.core.effects.UiEffectAwareState
import lab.maxb.dark.presentation.screens.core.effects.UiSideEffect
import lab.maxb.dark.presentation.screens.core.effects.UiSideEffectConsumed
import lab.maxb.dark.presentation.screens.core.effects.UiSideEffectsHolder

data class AddTaskUiState(
    val names: List<ItemHolder<String>> = listOf(ItemHolder("")),
    val images: List<Uri> = emptyList(),
    val suggestions: List<String> = emptyList(),
    val allowedImageCount: Int = RecognitionTask.MAX_IMAGES_COUNT,
    val isLoading: Boolean = false,
    override val sideEffectsHolder: UiSideEffectsHolder = EmptyEffectsHolder,
) : UiEffectAwareState {
    override fun clone(sideEffectsHolder: UiSideEffectsHolder)
        = copy(sideEffectsHolder = sideEffectsHolder)
}

sealed interface AddTaskUiEvent : UiEvent {
    data class NameChanged(val answer: ItemHolder<String>) : AddTaskUiEvent
    data class ImageChanged(val position: Int, val image: Uri) : AddTaskUiEvent
    data class ImageRemoved(val position: Int) : AddTaskUiEvent
    data class ImagesAdded(val images: List<Uri>) : AddTaskUiEvent
    object Submit : AddTaskUiEvent
    data class EffectConsumed(override val effect: EffectKey) : UiSideEffectConsumed, AddTaskUiEvent
}

sealed interface AddTaskUiSideEffect: UiSideEffect {
    data class UserMessage(val message: UiText) : AddTaskUiSideEffect
    object SubmitSuccess : AddTaskUiSideEffect
}