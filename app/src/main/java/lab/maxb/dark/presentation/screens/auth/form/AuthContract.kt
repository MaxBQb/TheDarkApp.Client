package lab.maxb.dark.presentation.screens.auth.form

import lab.maxb.dark.presentation.extra.UiText
import lab.maxb.dark.presentation.extra.UiTrigger
import lab.maxb.dark.presentation.extra.UiTriggers
import lab.maxb.dark.presentation.screens.core.UiEvent
import lab.maxb.dark.presentation.screens.core.UiState

data class AuthUiState(
    val login: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val passwordRepeat: String = "",
    val locale: String = "",
    val isAccountNew: Boolean = false,
    val isLoading: Boolean = false,
    val errors: UiTriggers<AuthUiEvent.Error> = UiTriggers(),
    val authorized: AuthUiEvent.Authorized? = null,
    val localeUpdated: AuthUiEvent.LocaleUpdated? = null,
) : UiState

sealed interface AuthUiEvent : UiEvent {
    data class LoginChanged(val login: String) : AuthUiEvent
    data class LocaleChanged(val locale: String) : AuthUiEvent
    data class PasswordChanged(val password: String) : AuthUiEvent
    data class PasswordRepeatChanged(val password: String) : AuthUiEvent
    data class PasswordVisibilityChanged(val showPassword: Boolean) : AuthUiEvent
    data class RegistrationNeededChanged(val isAccountNew: Boolean) : AuthUiEvent
    object Submit : AuthUiEvent

    // UiTriggers
    data class Error(val message: UiText) : UiTrigger(), AuthUiEvent
    data class LocaleUpdated(val locale: String) : UiTrigger(), AuthUiEvent
    object Authorized : UiTrigger(), AuthUiEvent
}