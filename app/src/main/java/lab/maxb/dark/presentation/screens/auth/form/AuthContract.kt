package lab.maxb.dark.presentation.screens.auth.form

import lab.maxb.dark.presentation.extra.UiText
import lab.maxb.dark.presentation.screens.core.UiEvent
import lab.maxb.dark.presentation.screens.core.effects.EffectKey
import lab.maxb.dark.presentation.screens.core.effects.EmptyEffectsHolder
import lab.maxb.dark.presentation.screens.core.effects.UiEffectAwareState
import lab.maxb.dark.presentation.screens.core.effects.UiSideEffect
import lab.maxb.dark.presentation.screens.core.effects.UiSideEffectConsumed
import lab.maxb.dark.presentation.screens.core.effects.UiSideEffectsHolder

data class AuthUiState(
    val login: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val passwordRepeat: String = "",
    val locale: String = "",
    val isAccountNew: Boolean = false,
    val isLoading: Boolean = false,
    override val sideEffectsHolder: UiSideEffectsHolder = EmptyEffectsHolder,
) : UiEffectAwareState {
    override fun clone(sideEffectsHolder: UiSideEffectsHolder)
        = copy(sideEffectsHolder=sideEffectsHolder)
}

sealed interface AuthUiEvent : UiEvent {
    data class LoginChanged(val login: String) : AuthUiEvent
    data class LocaleChanged(val locale: String) : AuthUiEvent
    data class PasswordChanged(val password: String) : AuthUiEvent
    data class PasswordRepeatChanged(val password: String) : AuthUiEvent
    data class PasswordVisibilityChanged(val showPassword: Boolean) : AuthUiEvent
    data class RegistrationNeededChanged(val isAccountNew: Boolean) : AuthUiEvent
    object Submit : AuthUiEvent

    data class EffectConsumed(override val effect: EffectKey): UiSideEffectConsumed, AuthUiEvent
}

sealed interface AuthUiSideEffect: UiSideEffect {
    data class Error(val message: UiText) : AuthUiSideEffect
    data class LocaleUpdated(val locale: String) : AuthUiSideEffect
    object Authorized : AuthUiSideEffect
}