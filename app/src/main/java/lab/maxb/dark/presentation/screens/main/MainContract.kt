package lab.maxb.dark.presentation.screens.main

import lab.maxb.dark.presentation.extra.Result


data class MainUiState(
    val authorized: Result<Boolean> = Result.Loading,
)
