package lab.maxb.dark.presentation.screens.task.solve

import lab.maxb.dark.presentation.extra.UiText
import lab.maxb.dark.presentation.extra.UiTrigger
import lab.maxb.dark.presentation.extra.UiTriggers

data class TaskSolveUiState(
    val answer: String = "",
    val isLoading: Boolean = false,
    val isReviewMode: Boolean = false,
    val isReviewed: Boolean = false,
    val images: List<Any?> = emptyList(),
    val userMessages: UiTriggers<TaskSolveUiEvent.UserMessage> = UiTriggers(),
    val taskNotFound: TaskSolveUiEvent.NoSuchTask? = null,
)

sealed interface TaskSolveUiEvent {
    data class AnswerChanged(val answer: String) : TaskSolveUiEvent
    object SubmitTaskSolveSolution : TaskSolveUiEvent
    data class MarkChanged(val isAllowed: Boolean) : TaskSolveUiEvent

    // UiTriggers
    data class UserMessage(val message: UiText): UiTrigger(), TaskSolveUiEvent
    object NoSuchTask: UiTrigger(), TaskSolveUiEvent
}