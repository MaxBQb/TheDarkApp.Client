package lab.maxb.dark.ui.screens.common

import lab.maxb.dark.ui.screens.core.UiState

interface LoadableState: UiState {
    val isLoading: Boolean
    fun clone(isLoading: Boolean): LoadableState
}

inline fun <reified T: LoadableState> T.copy(isLoading: Boolean)
    = clone(isLoading) as T

inline val <reified T: LoadableState> T.Loaded
    get() = copy(false)

inline val <reified T: LoadableState> T.Loading
    get() = copy(true)
