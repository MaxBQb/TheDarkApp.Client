package lab.maxb.dark.presentation.screens.drawer

import lab.maxb.dark.presentation.screens.core.UiState

interface DrawerUiContract {
    data class State(
        val destinations: List<DrawerDestination> = DrawerDestination.values().toList()
    ) : UiState
}
