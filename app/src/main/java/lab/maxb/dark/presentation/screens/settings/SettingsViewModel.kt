package lab.maxb.dark.presentation.screens.settings

import kotlinx.coroutines.flow.combine
import lab.maxb.dark.domain.usecase.settings.UseExternalSuggestionsUseCases
import lab.maxb.dark.domain.usecase.settings.locale.ChangeLocaleUseCase
import lab.maxb.dark.domain.usecase.settings.locale.GetCurrentLocaleUseCase
import lab.maxb.dark.presentation.extra.launch
import lab.maxb.dark.presentation.extra.stateIn
import lab.maxb.dark.presentation.screens.core.BaseViewModel
import lab.maxb.dark.presentation.screens.core.effects.withEffectTriggered
import org.koin.android.annotation.KoinViewModel


@KoinViewModel
class SettingsViewModel(
    getCurrentLocaleUseCase: GetCurrentLocaleUseCase,
    private val changeLocaleUseCase: ChangeLocaleUseCase,
    private val useExternalSuggestionsUseCases: UseExternalSuggestionsUseCases,
) : BaseViewModel<SettingsUiState, SettingsUiEvent, SettingsUiSideEffect>() {

    override fun getInitialState() = SettingsUiState()
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

    override fun handleEvent(event: SettingsUiEvent): Unit = with(event) {
        when (this) {
            is SettingsUiEvent.LocaleChanged -> changeLocale(locale)
            SettingsUiEvent.UseExternalSuggestionsToggled -> launch { useExternalSuggestionsUseCases.toggle() }
            is SettingsUiEvent.EffectConsumed -> handleEffectConsumption(this)
        }
    }

    private fun changeLocale(locale: String) {
        launch {
            val newLocale = changeLocaleUseCase(locale)
            setState {
                it.withEffectTriggered(SettingsUiSideEffect.LocaleUpdated(newLocale))
            }
        }
    }
}
