package lab.maxb.dark.presentation.screens.auth.form

import lab.maxb.dark.presentation.extra.UiText
import lab.maxb.dark.presentation.extra.UiTrigger
import lab.maxb.dark.presentation.extra.UiTriggers

data class AuthUiState(
    val login: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val passwordRepeat: String = "",
    val isAccountNew: Boolean = false,
    val isLoading: Boolean = false,
    val errors: UiTriggers<AuthUiEvent.Error> = UiTriggers(),
    val authorized: AuthUiEvent.Authorized? = null,
)

sealed interface AuthUiEvent {
    data class LoginChanged(val login: String) : AuthUiEvent
    data class PasswordChanged(val password: String) : AuthUiEvent
    data class PasswordRepeatChanged(val password: String) : AuthUiEvent
    data class PasswordVisibilityChanged(val showPassword: Boolean) : AuthUiEvent
    data class RegistrationNeededChanged(val isAccountNew: Boolean) : AuthUiEvent
    object Submit : AuthUiEvent

    // UiTriggers
    data class Error(val message: UiText) : UiTrigger(), AuthUiEvent
    object Authorized : UiTrigger(), AuthUiEvent
}