package lab.maxb.dark.ui.screens.drawer

import lab.maxb.dark.ui.model.DrawerDestination
import lab.maxb.dark.ui.screens.core.UiState

interface DrawerUiContract {
    data class State(
        val destinations: List<DrawerDestination> = DrawerDestination.values().toList()
    ) : UiState
}
