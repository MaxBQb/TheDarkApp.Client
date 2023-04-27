package lab.maxb.dark.presentation.screens.main

import lab.maxb.dark.presentation.extra.Result
import lab.maxb.dark.presentation.screens.core.UiEvent
import lab.maxb.dark.presentation.screens.core.UiState


data class MainUiState(
    val authorized: Result<Boolean> = Result.Loading,
) : UiState

sealed interface MainUiEvent : UiEvent
