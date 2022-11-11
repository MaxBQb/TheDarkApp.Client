package lab.maxb.dark.presentation.screens.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import lab.maxb.dark.domain.usecase.settings.locale.ChangeLocaleUseCase
import lab.maxb.dark.domain.usecase.settings.locale.GetCurrentLocaleUseCase
import lab.maxb.dark.presentation.extra.launch
import lab.maxb.dark.presentation.extra.stateIn
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    getCurrentLocaleUseCase: GetCurrentLocaleUseCase,
    private val changeLocaleUseCase: ChangeLocaleUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = combine(_uiState, getCurrentLocaleUseCase()) { state, locale ->
        state.copy(
            locale = locale,
        )
    }.stateIn(SettingsUiState())

    fun onEvent(event: SettingsUiEvent) = with(event) {
        when (this) {
            is SettingsUiEvent.LocaleChanged -> changeLocale(locale)
            is SettingsUiEvent.LocaleUpdated -> _uiState.update { it.copy(localeUpdated = null) }
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
