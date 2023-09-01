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
import lab.maxb.dark.presentation.screens.auth.handle.AuthHandleUiContract as Ui


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
