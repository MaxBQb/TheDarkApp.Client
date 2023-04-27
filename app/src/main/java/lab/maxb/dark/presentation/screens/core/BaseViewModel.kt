package lab.maxb.dark.presentation.screens.core

import kotlinx.coroutines.flow.StateFlow

interface BaseViewModel<S: UiState, E: UiEvent> {
    val uiState: StateFlow<S>
    fun onEvent(event: E)
}

