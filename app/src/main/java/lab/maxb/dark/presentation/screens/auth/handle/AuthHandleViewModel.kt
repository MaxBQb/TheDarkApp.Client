package lab.maxb.dark.presentation.screens.auth.handle

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import lab.maxb.dark.domain.usecase.auth.HandleInitialAuthUseCase
import lab.maxb.dark.domain.usecase.profile.RefreshProfileUseCase
import lab.maxb.dark.presentation.extra.FirstOnly
import lab.maxb.dark.presentation.extra.launch
import lab.maxb.dark.presentation.extra.stateIn
import lab.maxb.dark.presentation.extra.stateInAsResult
import javax.inject.Inject


@HiltViewModel
class AuthHandleViewModel @Inject constructor(
    handleInitialAuthUseCase: HandleInitialAuthUseCase,
    private val refreshProfileUseCase: RefreshProfileUseCase,
) : ViewModel() {
    private val isAuthorized = handleInitialAuthUseCase().stateInAsResult()
    private var retryRequest by FirstOnly()

    private val _uiState = MutableStateFlow(AuthHandleUiState())
    val uiState = combine(_uiState, isAuthorized) { state, isAuthorized ->
        state.copy(authorized = isAuthorized)
    }.stateIn(AuthHandleUiState())

    fun onEvent(event: AuthHandleUiEvent) = when(event) {
        is AuthHandleUiEvent.Retry -> retry()
    }

    private fun retry() { retryRequest = launch {
        refreshProfileUseCase()
    } }
}
