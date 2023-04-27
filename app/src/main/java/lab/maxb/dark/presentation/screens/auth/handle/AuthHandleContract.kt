package lab.maxb.dark.presentation.screens.auth.handle
import lab.maxb.dark.presentation.extra.Result
import lab.maxb.dark.presentation.screens.core.UiEvent
import lab.maxb.dark.presentation.screens.core.UiState

data class AuthHandleUiState(
    val authorized: Result<Boolean> = Result.Loading,
) : UiState

sealed interface AuthHandleUiEvent : UiEvent {
    object Retry: AuthHandleUiEvent
}
