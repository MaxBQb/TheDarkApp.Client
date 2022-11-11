package lab.maxb.dark.presentation.screens.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.runBlocking
import lab.maxb.dark.domain.usecase.auth.HandleInitialAuthUseCase
import lab.maxb.dark.domain.usecase.settings.locale.HandleCurrentLocaleUseCase
import lab.maxb.dark.presentation.extra.stateIn
import lab.maxb.dark.presentation.extra.stateInAsResult
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    handleInitialAuthUseCase: HandleInitialAuthUseCase,
    private val handleCurrentLocaleUseCase: HandleCurrentLocaleUseCase,
) : ViewModel() {
    private val isAuthorized = handleInitialAuthUseCase().stateInAsResult()
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = combine(_uiState, isAuthorized) { state, isAuthorized ->
        state.copy(authorized = isAuthorized)
    }.stateIn(MainUiState())

    fun getLocale(current: String, system: String) = runBlocking {
        handleCurrentLocaleUseCase(current, system)
    }
}
