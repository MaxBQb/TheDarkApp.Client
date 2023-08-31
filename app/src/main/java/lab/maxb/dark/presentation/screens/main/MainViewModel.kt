package lab.maxb.dark.presentation.screens.main

import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.runBlocking
import lab.maxb.dark.domain.usecase.auth.HandleInitialAuthUseCase
import lab.maxb.dark.domain.usecase.settings.locale.HandleCurrentLocaleUseCase
import lab.maxb.dark.presentation.extra.stateIn
import lab.maxb.dark.presentation.extra.stateInAsResult
import lab.maxb.dark.presentation.screens.core.StatefulViewModel
import org.koin.android.annotation.KoinViewModel


@KoinViewModel
class MainViewModel(
    handleInitialAuthUseCase: HandleInitialAuthUseCase,
    private val handleCurrentLocaleUseCase: HandleCurrentLocaleUseCase,
) : StatefulViewModel<MainUiState>() {
    private val isAuthorized = handleInitialAuthUseCase().stateInAsResult()
    override fun getInitialState() = MainUiState()
    override val uiState = combine(_uiState, isAuthorized) { state, isAuthorized ->
        state.copy(authorized = isAuthorized)
    }.stateIn(MainUiState())

    fun getLocale(current: String, system: String) = runBlocking {
        handleCurrentLocaleUseCase(current, system)
    }
}
