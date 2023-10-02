package lab.maxb.dark.ui.screens.auth_handle

import kotlinx.coroutines.flow.combine
import lab.maxb.dark.domain.usecase.HandleInitialAuthUseCase
import lab.maxb.dark.domain.usecase.RefreshProfileUseCase
import lab.maxb.dark.ui.extra.FirstOnly
import lab.maxb.dark.ui.extra.launch
import lab.maxb.dark.ui.extra.stateIn
import lab.maxb.dark.ui.extra.stateInAsResult
import lab.maxb.dark.ui.screens.core.PureInteractiveViewModel
import org.koin.android.annotation.KoinViewModel
import lab.maxb.dark.ui.screens.auth_handle.AuthHandleUiContract as Ui


@KoinViewModel
class AuthHandleViewModel(
    handleInitialAuthUseCase: HandleInitialAuthUseCase,
    private val refreshProfileUseCase: RefreshProfileUseCase,
) : PureInteractiveViewModel<Ui.State, Ui.Event>() {
    private val isAuthorized = handleInitialAuthUseCase().stateInAsResult()
    private var retryRequest by FirstOnly()

    override fun getInitialState() = Ui.State()
    override val uiState = combine(_uiState, isAuthorized) { state, isAuthorized ->
        state.copy(authorized = isAuthorized)
    }.stateIn(Ui.State())

    override fun handleEvent(event: Ui.Event) = when(event) {
        is Ui.Event.Retry -> retry()
    }

    private fun retry() { retryRequest = launch {
        refreshProfileUseCase()
    } }
}
