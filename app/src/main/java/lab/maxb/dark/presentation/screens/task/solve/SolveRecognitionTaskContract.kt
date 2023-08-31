package lab.maxb.dark.presentation.screens.task.solve

import lab.maxb.dark.presentation.extra.UiText
import lab.maxb.dark.presentation.screens.core.UiEvent
import lab.maxb.dark.presentation.screens.core.effects.EffectKey
import lab.maxb.dark.presentation.screens.core.effects.EmptyEffectsHolder
import lab.maxb.dark.presentation.screens.core.effects.UiEffectAwareState
import lab.maxb.dark.presentation.screens.core.effects.UiSideEffect
import lab.maxb.dark.presentation.screens.core.effects.UiSideEffectConsumed
import lab.maxb.dark.presentation.screens.core.effects.UiSideEffectsHolder

data class TaskSolveUiState(
    val answer: String = "",
    val isLoading: Boolean = false,
    val isReviewMode: Boolean = false,
    val isReviewed: Boolean = false,
    val isFavorite: Boolean? = null,
    val zoomEnabled: Boolean = false,
    val images: List<Any?> = emptyList(),
    override val sideEffectsHolder: UiSideEffectsHolder = EmptyEffectsHolder,
) : UiEffectAwareState {
    override fun clone(sideEffectsHolder: UiSideEffectsHolder)
        = copy(sideEffectsHolder=sideEffectsHolder)
}

sealed interface TaskSolveUiEvent : UiEvent {
    data class AnswerChanged(val answer: String) : TaskSolveUiEvent
    object SubmitTaskSolveSolution : TaskSolveUiEvent
    data class ZoomToggled(val zoomEnabled: Boolean) : TaskSolveUiEvent
    data class MarkChanged(val isAllowed: Boolean) : TaskSolveUiEvent
    data class MarkFavorite(val id: String, val isFavorite: Boolean) : TaskSolveUiEvent
    data class EffectConsumed(override val effect: EffectKey) : UiSideEffectConsumed, TaskSolveUiEvent
}

sealed interface TaskSolveUiSideEffect: UiSideEffect {
    data class UserMessage(val message: UiText): TaskSolveUiSideEffect
    object NoSuchTask: TaskSolveUiSideEffect
}