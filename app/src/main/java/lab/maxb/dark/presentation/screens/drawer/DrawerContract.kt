package lab.maxb.dark.presentation.screens.drawer

import lab.maxb.dark.presentation.screens.core.UiState

data class DrawerUiState(
    val destinations: List<DrawerDestination> = DrawerDestination.values().toList()
) : UiState
