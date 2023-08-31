package lab.maxb.dark.presentation.screens.auth.handle

import kotlinx.coroutines.flow.combine
import lab.maxb.dark.domain.usecase.auth.HandleInitialAuthUseCase
import lab.maxb.dark.domain.usecase.profile.RefreshProfileUseCase
import lab.maxb.dark.presentation.extra.FirstOnly
import lab.maxb.dark.presentation.extra.launch
import lab.maxb.dark.presentation.extra.stateIn
import lab.maxb.dark.presentation.extra.stateInAsResult
import lab.maxb.dark.presentation.screens.core.PureInteractiveViewModel
import org.koin.android.annotation.KoinViewModel


@KoinViewModel
class AuthHandleViewModel(
    handleInitialAuthUseCase: HandleInitialAuthUseCase,
    private val refreshProfileUseCase: RefreshProfileUseCase,
) : PureInteractiveViewModel<AuthHandleUiState, AuthHandleUiEvent>() {
    private val isAuthorized = handleInitialAuthUseCase().stateInAsResult()
    private var retryRequest by FirstOnly()

    override fun getInitialState() = AuthHandleUiState()
    override val uiState = combine(_uiState, isAuthorized) { state, isAuthorized ->
        state.copy(authorized = isAuthorized)
    }.stateIn(AuthHandleUiState())

    override fun handleEvent(event: AuthHandleUiEvent) = when(event) {
        is AuthHandleUiEvent.Retry -> retry()
    }

    private fun retry() { retryRequest = launch {
        refreshProfileUseCase()
    } }
}
