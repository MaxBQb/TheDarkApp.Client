package lab.maxb.dark.presentation.screens.task.add

import android.net.Uri
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.presentation.extra.ItemHolder
import lab.maxb.dark.presentation.extra.UiText
import lab.maxb.dark.presentation.screens.common.LoadableState
import lab.maxb.dark.presentation.screens.core.UiEvent
import lab.maxb.dark.presentation.screens.core.effects.EffectKey
import lab.maxb.dark.presentation.screens.core.effects.EmptyEffectsHolder
import lab.maxb.dark.presentation.screens.core.effects.UiEffectAwareState
import lab.maxb.dark.presentation.screens.core.effects.UiSideEffect
import lab.maxb.dark.presentation.screens.core.effects.UiSideEffectConsumed
import lab.maxb.dark.presentation.screens.core.effects.UiSideEffectsHolder

interface AddTaskUiContract {
    data class State(
        val names: List<ItemHolder<String>> = listOf(ItemHolder("")),
        val images: List<Uri> = emptyList(),
        val suggestions: List<String> = emptyList(),
        val allowedImageCount: Int = RecognitionTask.MAX_IMAGES_COUNT,
        override val isLoading: Boolean = false,
        override val sideEffectsHolder: UiSideEffectsHolder = EmptyEffectsHolder,
    ) : UiEffectAwareState, LoadableState {
        override fun clone(sideEffectsHolder: UiSideEffectsHolder) =
            copy(sideEffectsHolder = sideEffectsHolder)

        override fun clone(isLoading: Boolean) = copy(isLoading = isLoading)
    }

    sealed interface Event : UiEvent {
        data class NameChanged(val answer: ItemHolder<String>) : Event
        data class ImageChanged(val position: Int, val image: Uri) : Event
        data class ImageRemoved(val position: Int) : Event
        data class ImagesAdded(val images: List<Uri>) : Event
        object Submit : Event
        data class EffectConsumed(override val effect: EffectKey) : UiSideEffectConsumed, Event
    }

    sealed interface SideEffect : UiSideEffect {
        data class UserMessage(val message: UiText) : SideEffect
        object SubmitSuccess : SideEffect
    }
}