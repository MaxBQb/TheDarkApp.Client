package lab.maxb.dark.presentation.screens.settings

import lab.maxb.dark.presentation.screens.core.UiEvent
import lab.maxb.dark.presentation.screens.core.effects.EffectKey
import lab.maxb.dark.presentation.screens.core.effects.EmptyEffectsHolder
import lab.maxb.dark.presentation.screens.core.effects.UiEffectAwareState
import lab.maxb.dark.presentation.screens.core.effects.UiSideEffect
import lab.maxb.dark.presentation.screens.core.effects.UiSideEffectConsumed
import lab.maxb.dark.presentation.screens.core.effects.UiSideEffectsHolder

data class SettingsUiState(
    val locale: String = "",
    val useExternalSuggestions: Boolean = false,
    override val sideEffectsHolder: UiSideEffectsHolder = EmptyEffectsHolder,
) : UiEffectAwareState {
    override fun clone(sideEffectsHolder: UiSideEffectsHolder)
        = copy(sideEffectsHolder=sideEffectsHolder)
}

sealed interface SettingsUiEvent : UiEvent {
    data class LocaleChanged(val locale: String): SettingsUiEvent
    object UseExternalSuggestionsToggled: SettingsUiEvent
    data class EffectConsumed(override val effect: EffectKey) : UiSideEffectConsumed,
        SettingsUiEvent
}

sealed interface SettingsUiSideEffect : UiSideEffect {
    data class LocaleUpdated(val locale: String) : SettingsUiSideEffect
}