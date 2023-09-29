package lab.maxb.dark.ui.screens.settings

import lab.maxb.dark.ui.screens.core.UiEvent
import lab.maxb.dark.ui.screens.core.effects.EffectKey
import lab.maxb.dark.ui.screens.core.effects.EmptyEffectsHolder
import lab.maxb.dark.ui.screens.core.effects.UiEffectAwareState
import lab.maxb.dark.ui.screens.core.effects.UiSideEffect
import lab.maxb.dark.ui.screens.core.effects.UiSideEffectConsumed
import lab.maxb.dark.ui.screens.core.effects.UiSideEffectsHolder

interface SettingsUiContract {
    data class State(
        val locale: String = "",
        val useExternalSuggestions: Boolean = false,
        override val sideEffectsHolder: UiSideEffectsHolder = EmptyEffectsHolder,
    ) : UiEffectAwareState {
        override fun clone(sideEffectsHolder: UiSideEffectsHolder) =
            copy(sideEffectsHolder = sideEffectsHolder)
    }

    sealed interface Event : UiEvent {
        data class LocaleChanged(val locale: String) : Event
        object UseExternalSuggestionsToggled : Event
        data class EffectConsumed(override val effect: EffectKey) : UiSideEffectConsumed, Event
    }

    sealed interface SideEffect : UiSideEffect {
        data class LocaleUpdated(val locale: String) : SideEffect
    }
}