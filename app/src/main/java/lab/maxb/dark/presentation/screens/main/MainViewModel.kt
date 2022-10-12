package lab.maxb.dark.presentation.screens.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import lab.maxb.dark.domain.usecase.auth.HandleInitialAuthUseCase
import lab.maxb.dark.presentation.extra.stateIn
import lab.maxb.dark.presentation.extra.stateInAsResult
import org.koin.android.annotation.KoinViewModel


@KoinViewModel
class MainViewModel(
    handleInitialAuthUseCase: HandleInitialAuthUseCase,
) : ViewModel() {
    private val isAuthorized = handleInitialAuthUseCase().stateInAsResult()
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = combine(_uiState, isAuthorized) { state, isAuthorized ->
        state.copy(authorized = isAuthorized)
    }.stateIn(MainUiState())
}
