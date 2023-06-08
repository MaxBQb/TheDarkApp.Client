package lab.maxb.dark.presentation.screens.settings

import lab.maxb.dark.presentation.extra.UiTrigger
import lab.maxb.dark.presentation.screens.core.UiEvent
import lab.maxb.dark.presentation.screens.core.UiState

data class SettingsUiState(
    val locale: String = "",
    val useExternalSuggestions: Boolean = false,
    val localeUpdated: SettingsUiEvent.LocaleUpdated? = null,
) : UiState

sealed interface SettingsUiEvent : UiEvent {
    data class LocaleChanged(val locale: String): SettingsUiEvent
    object UseExternalSuggestionsToggled: SettingsUiEvent

    // UiTriggers
    data class LocaleUpdated(val locale: String) : UiTrigger(), SettingsUiEvent
}