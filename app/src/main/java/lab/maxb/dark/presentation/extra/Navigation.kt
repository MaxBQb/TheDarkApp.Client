package lab.maxb.dark.presentation.extra

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import lab.maxb.dark.presentation.screens.NavGraphs
import lab.maxb.dark.presentation.screens.appCurrentDestinationAsState
import lab.maxb.dark.presentation.screens.destinations.Destination
import lab.maxb.dark.presentation.screens.startAppDestination

val NavController.localDestination @Composable get(): Destination
    = appCurrentDestinationAsState().value
    ?: NavGraphs.root.startAppDestination