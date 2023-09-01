package lab.maxb.dark.presentation.screens.auth.form

import lab.maxb.dark.presentation.extra.UiText
import lab.maxb.dark.presentation.screens.core.UiEvent
import lab.maxb.dark.presentation.screens.core.effects.EffectKey
import lab.maxb.dark.presentation.screens.core.effects.EmptyEffectsHolder
import lab.maxb.dark.presentation.screens.core.effects.UiEffectAwareState
import lab.maxb.dark.presentation.screens.core.effects.UiSideEffect
import lab.maxb.dark.presentation.screens.core.effects.UiSideEffectConsumed
import lab.maxb.dark.presentation.screens.core.effects.UiSideEffectsHolder

interface AuthUiContract {
    data class State(
        val login: String = "",
        val password: String = "",
        val showPassword: Boolean = false,
        val passwordRepeat: String = "",
        val locale: String = "",
        val isAccountNew: Boolean = false,
        val isLoading: Boolean = false,
        override val sideEffectsHolder: UiSideEffectsHolder = EmptyEffectsHolder,
    ) : UiEffectAwareState {
        override fun clone(sideEffectsHolder: UiSideEffectsHolder) =
            copy(sideEffectsHolder = sideEffectsHolder)
    }

    sealed interface Event : UiEvent {
        data class LoginChanged(val login: String) : Event
        data class LocaleChanged(val locale: String) : Event
        data class PasswordChanged(val password: String) : Event
        data class PasswordRepeatChanged(val password: String) : Event
        data class PasswordVisibilityChanged(val showPassword: Boolean) : Event
        data class RegistrationNeededChanged(val isAccountNew: Boolean) : Event
        object Submit : Event

        data class EffectConsumed(override val effect: EffectKey) : UiSideEffectConsumed, Event
    }

    sealed interface SideEffect : UiSideEffect {
        data class Error(val message: UiText) : SideEffect
        data class LocaleUpdated(val locale: String) : SideEffect
        object Authorized : SideEffect
    }
}