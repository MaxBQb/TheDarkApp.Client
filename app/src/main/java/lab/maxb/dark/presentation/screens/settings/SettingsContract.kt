package lab.maxb.dark.presentation.screens.settings

import lab.maxb.dark.presentation.extra.UiTrigger

data class SettingsUiState(
    val locale: String = "",
    val localeUpdated: SettingsUiEvent.LocaleUpdated? = null,
)

sealed interface SettingsUiEvent {
    data class LocaleChanged(val locale: String): SettingsUiEvent

    // UiTriggers
    data class LocaleUpdated(val locale: String) : UiTrigger(), SettingsUiEvent
}