package lab.maxb.dark.ui.extra

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import lab.maxb.dark.ui.screens.NavGraphs
import lab.maxb.dark.ui.screens.appCurrentDestinationAsState
import lab.maxb.dark.ui.screens.destinations.Destination
import lab.maxb.dark.ui.screens.destinations.DirectionDestination
import lab.maxb.dark.ui.screens.startAppDestination

val NavController.localDestination @Composable get(): Destination
    = appCurrentDestinationAsState().value
    ?: NavGraphs.root.startAppDestination


fun NavOptionsBuilder.dropStack(from: Destination) {
    popUpTo(from) { inclusive = true }
}

fun NavOptionsBuilder.asInitial(dropFrom: Destination) {
    launchSingleTop = true
    dropStack(dropFrom)
}

fun DestinationsNavigator.initialNavigate(
    direction: DirectionDestination,
    from: DirectionDestination,
) = navigate(direction) { asInitial(from) }

fun NavController.initialNavigate(
    direction: DirectionDestination,
    from: Destination,
) = navigate(direction.route) { asInitial(from) }

