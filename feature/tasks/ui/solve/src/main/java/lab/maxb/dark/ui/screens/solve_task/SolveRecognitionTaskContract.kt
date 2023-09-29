package lab.maxb.dark.ui.screens.solve_task

import lab.maxb.dark.ui.extra.UiText
import lab.maxb.dark.ui.screens.common.LoadableState
import lab.maxb.dark.ui.screens.core.UiEvent
import lab.maxb.dark.ui.screens.core.effects.EffectKey
import lab.maxb.dark.ui.screens.core.effects.EmptyEffectsHolder
import lab.maxb.dark.ui.screens.core.effects.UiEffectAwareState
import lab.maxb.dark.ui.screens.core.effects.UiSideEffect
import lab.maxb.dark.ui.screens.core.effects.UiSideEffectConsumed
import lab.maxb.dark.ui.screens.core.effects.UiSideEffectsHolder

interface TaskSolveUiContract {
    data class State(
        val answer: String = "",
        override val isLoading: Boolean = false,
        val isReviewMode: Boolean = false,
        val isReviewed: Boolean = false,
        val isFavorite: Boolean? = null,
        val zoomEnabled: Boolean = false,
        val images: List<Any?> = emptyList(),
        override val sideEffectsHolder: UiSideEffectsHolder = EmptyEffectsHolder,
    ) : UiEffectAwareState, LoadableState {
        override fun clone(sideEffectsHolder: UiSideEffectsHolder) =
            copy(sideEffectsHolder = sideEffectsHolder)

        override fun clone(isLoading: Boolean) = copy(isLoading = isLoading)
    }

    sealed interface Event : UiEvent {
        data class AnswerChanged(val answer: String) : Event
        object SubmitTaskSolveSolution : Event
        data class ZoomToggled(val zoomEnabled: Boolean) : Event
        data class MarkChanged(val isAllowed: Boolean) : Event
        data class MarkFavorite(val id: String, val isFavorite: Boolean) : Event
        data class EffectConsumed(override val effect: EffectKey) : UiSideEffectConsumed, Event
    }

    sealed interface SideEffect : UiSideEffect {
        data class UserMessage(val message: UiText) : SideEffect
        object NoSuchTask : SideEffect
    }
}