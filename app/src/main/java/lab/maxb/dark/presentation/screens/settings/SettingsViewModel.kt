package lab.maxb.dark.presentation.screens.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import lab.maxb.dark.domain.usecase.settings.UseExternalSuggestionsUseCases
import lab.maxb.dark.domain.usecase.settings.locale.ChangeLocaleUseCase
import lab.maxb.dark.domain.usecase.settings.locale.GetCurrentLocaleUseCase
import lab.maxb.dark.presentation.extra.launch
import lab.maxb.dark.presentation.extra.stateIn
import lab.maxb.dark.presentation.screens.core.BaseViewModel
import org.koin.android.annotation.KoinViewModel


@KoinViewModel
class SettingsViewModel(
    getCurrentLocaleUseCase: GetCurrentLocaleUseCase,
    private val changeLocaleUseCase: ChangeLocaleUseCase,
    private val useExternalSuggestionsUseCases: UseExternalSuggestionsUseCases,
) : BaseViewModel<SettingsUiState, SettingsUiEvent>, ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    override val uiState = combine(
        _uiState,
        getCurrentLocaleUseCase(),
        useExternalSuggestionsUseCases.get(),
    ) { state, locale, useExternalSuggestions ->
        state.copy(
            locale = locale,
            useExternalSuggestions = useExternalSuggestions,
        )
    }.stateIn(SettingsUiState())

    override fun onEvent(event: SettingsUiEvent): Unit = with(event) {
        when (this) {
            is SettingsUiEvent.LocaleChanged -> changeLocale(locale)
            is SettingsUiEvent.LocaleUpdated -> _uiState.update { it.copy(localeUpdated = null) }
            SettingsUiEvent.UseExternalSuggestionsToggled -> launch { useExternalSuggestionsUseCases.toggle() }
        }
    }

    private fun changeLocale(locale: String) {
        launch {
            val newLocale = changeLocaleUseCase(locale)
            _uiState.update { it.copy(
                localeUpdated = SettingsUiEvent.LocaleUpdated(newLocale)
            ) }
        }
    }
}
