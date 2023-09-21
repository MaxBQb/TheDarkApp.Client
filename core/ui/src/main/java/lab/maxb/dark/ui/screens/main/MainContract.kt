package lab.maxb.dark.ui.screens.main

import lab.maxb.dark.ui.extra.Result
import lab.maxb.dark.ui.screens.core.UiState

interface MainUiContract {
    data class State(
        val authorized: Result<Boolean> = Result.Loading,
    ) : UiState
}