package lab.maxb.dark.ui.screens.settings

import kotlinx.coroutines.flow.combine
import lab.maxb.dark.domain.usecase.UseExternalSuggestionsUseCases
import lab.maxb.dark.domain.usecase.ChangeLocaleUseCase
import lab.maxb.dark.domain.usecase.GetCurrentLocaleUseCase
import lab.maxb.dark.ui.extra.launch
import lab.maxb.dark.ui.extra.stateIn
import lab.maxb.dark.ui.screens.core.BaseViewModel
import org.koin.android.annotation.KoinViewModel
import lab.maxb.dark.ui.screens.settings.SettingsUiContract as Ui


@KoinViewModel
class SettingsViewModel(
    getCurrentLocaleUseCase: GetCurrentLocaleUseCase,
    private val changeLocaleUseCase: ChangeLocaleUseCase,
    private val useExternalSuggestionsUseCases: UseExternalSuggestionsUseCases,
) : BaseViewModel<Ui.State, Ui.Event, Ui.SideEffect>() {

    override fun getInitialState() = Ui.State()
    override val uiState = combine(
        _uiState,
        getCurrentLocaleUseCase(),
        useExternalSuggestionsUseCases.get(),
    ) { state, locale, useExternalSuggestions ->
        state.copy(
            locale = locale,
            useExternalSuggestions = useExternalSuggestions,
        )
    }.stateIn(Ui.State())

    override fun handleEvent(event: Ui.Event): Unit = with(event) {
        when (this) {
            is Ui.Event.LocaleChanged -> changeLocale(locale)
            Ui.Event.UseExternalSuggestionsToggled -> launch { useExternalSuggestionsUseCases.toggle() }
            is Ui.Event.EffectConsumed -> handleEffectConsumption(this)
        }
    }

    private fun changeLocale(locale: String) {
        launch {
            val newLocale = changeLocaleUseCase(locale)
            setEffect { Ui.SideEffect.LocaleUpdated(newLocale) }
        }
    }
}
