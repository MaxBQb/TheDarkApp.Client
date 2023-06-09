package lab.maxb.dark.presentation.screens.task.solve

import lab.maxb.dark.presentation.extra.UiText
import lab.maxb.dark.presentation.extra.UiTrigger
import lab.maxb.dark.presentation.extra.UiTriggers
import lab.maxb.dark.presentation.screens.core.UiEvent
import lab.maxb.dark.presentation.screens.core.UiState

data class TaskSolveUiState(
    val answer: String = "",
    val isLoading: Boolean = false,
    val isReviewMode: Boolean = false,
    val isReviewed: Boolean = false,
    val isFavorite: Boolean? = null,
    val zoomEnabled: Boolean = false,
    val images: List<Any?> = emptyList(),
    val userMessages: UiTriggers<TaskSolveUiEvent.UserMessage> = UiTriggers(),
    val taskNotFound: TaskSolveUiEvent.NoSuchTask? = null,
) : UiState

sealed interface TaskSolveUiEvent : UiEvent {
    data class AnswerChanged(val answer: String) : TaskSolveUiEvent
    object SubmitTaskSolveSolution : TaskSolveUiEvent
    data class ZoomToggled(val zoomEnabled: Boolean) : TaskSolveUiEvent
    data class MarkChanged(val isAllowed: Boolean) : TaskSolveUiEvent
    data class MarkFavorite(val id: String, val isFavorite: Boolean) : TaskSolveUiEvent

    // UiTriggers
    data class UserMessage(val message: UiText): UiTrigger(), TaskSolveUiEvent
    object NoSuchTask: UiTrigger(), TaskSolveUiEvent
}