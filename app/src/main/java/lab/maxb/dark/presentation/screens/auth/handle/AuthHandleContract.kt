package lab.maxb.dark.presentation.screens.auth.handle
import lab.maxb.dark.presentation.extra.Result

data class AuthHandleUiState(
    val authorized: Result<Boolean> = Result.Loading,
)
