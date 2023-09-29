package lab.maxb.dark.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import org.koin.compose.koinInject

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    DestinationsNavHost(
        navController = navController,
        navGraph = koinInject<RootNavigationGraph>(),
        modifier = modifier,
        dependenciesContainerBuilder = {
            dependency(navController.asDarkNavigator())
        }
    )
}