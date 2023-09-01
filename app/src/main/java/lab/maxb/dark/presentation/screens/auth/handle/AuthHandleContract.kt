package lab.maxb.dark.presentation.screens.auth.handle
import lab.maxb.dark.presentation.extra.Result
import lab.maxb.dark.presentation.screens.core.UiEvent
import lab.maxb.dark.presentation.screens.core.UiState


interface AuthHandleUiContract {
    data class State(
        val authorized: Result<Boolean> = Result.Loading,
    ) : UiState

    sealed interface Event : UiEvent {
        object Retry : Event
    }
}
