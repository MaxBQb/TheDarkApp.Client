package lab.maxb.dark.ui.screens.auth.handle
import lab.maxb.dark.ui.extra.Result
import lab.maxb.dark.ui.screens.core.UiEvent
import lab.maxb.dark.ui.screens.core.UiState


interface AuthHandleUiContract {
    data class State(
        val authorized: Result<Boolean> = Result.Loading,
    ) : UiState

    sealed interface Event : UiEvent {
        object Retry : Event
    }
}
