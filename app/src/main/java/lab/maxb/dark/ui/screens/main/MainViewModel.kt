package lab.maxb.dark.ui.screens.main

import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.runBlocking
import lab.maxb.dark.domain.usecase.auth.HandleInitialAuthUseCase
import lab.maxb.dark.domain.usecase.settings.locale.HandleCurrentLocaleUseCase
import lab.maxb.dark.ui.extra.stateIn
import lab.maxb.dark.ui.extra.stateInAsResult
import lab.maxb.dark.ui.screens.core.StatefulViewModel
import org.koin.android.annotation.KoinViewModel
import lab.maxb.dark.ui.screens.main.MainUiContract as Ui


@KoinViewModel
class MainViewModel(
    handleInitialAuthUseCase: HandleInitialAuthUseCase,
    private val handleCurrentLocaleUseCase: HandleCurrentLocaleUseCase,
) : StatefulViewModel<Ui.State>() {
    private val isAuthorized = handleInitialAuthUseCase().stateInAsResult()
    override fun getInitialState() = Ui.State()
    override val uiState = combine(_uiState, isAuthorized) { state, isAuthorized ->
        state.copy(authorized = isAuthorized)
    }.stateIn(Ui.State())

    fun getLocale(current: String, system: String) = runBlocking {
        handleCurrentLocaleUseCase(current, system)
    }
}
