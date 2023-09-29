package lab.maxb.dark.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.Direction
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import com.ramcosta.composedestinations.utils.navGraph
import com.ramcosta.composedestinations.utils.startDestination
import lab.maxb.dark.ui.screens.add_task.destinations.AddRecognitionTaskScreenDestination
import lab.maxb.dark.ui.screens.articles.destinations.ArticlesScreenDestination
import lab.maxb.dark.ui.screens.auth_form.AuthNavigator
import lab.maxb.dark.ui.screens.auth_handle.AuthHandleNavigator
import lab.maxb.dark.ui.screens.destinations.AuthScreenDestination
import lab.maxb.dark.ui.screens.drawer.DrawerDestination
import lab.maxb.dark.ui.screens.drawer.DrawerDestination.*
import lab.maxb.dark.ui.screens.drawer.DrawerNavigator
import lab.maxb.dark.ui.screens.settings.destinations.SettingsScreenDestination
import lab.maxb.dark.ui.screens.solve_task.destinations.SolveRecognitionTaskScreenDestination
import lab.maxb.dark.ui.screens.tasks_list.TaskListNavigator
import lab.maxb.dark.ui.screens.tasks_list.destinations.FavoriteRecognitionTaskListScreenDestination
import lab.maxb.dark.ui.screens.tasks_list.destinations.RecognitionTaskListScreenDestination
import lab.maxb.dark.ui.screens.welcome.WelcomeNavigator
import lab.maxb.dark.ui.screens.welcome.destinations.WelcomeScreenDestination
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.Named

interface DarkNavigator :
    AuthNavigator,
    AuthHandleNavigator,
    WelcomeNavigator,
    TaskListNavigator,
    DrawerNavigator,
    BaseNavigator

@Named(CORE_NAVIGATOR)
@Factory([DarkNavigator::class, BaseNavigator::class])
class DarkNavigatorImpl(
    @InjectedParam private val navController: NavController,
) : DarkNavigator {
    override fun initialNavigate(
        from: DestinationSpec<*>,
        to: Direction,
    ) = navController.navigate(to) {
        launchSingleTop = true
        popUpTo(from) { inclusive = true }
    }

    override fun navigate(to: Direction)= navController.navigate(to)
    override fun navigateUp() { navController.navigateUp() }

    override val currentDestination
        @Composable get() = navController.currentDestinationAsState().value
            ?: navController.navGraph.startDestination

    override fun flatNavigate(to: DestinationSpec<*>) = navController.navigate(to.route) {
        launchSingleTop = true
    }

    override fun findDestination(item: DrawerDestination): DirectionDestinationSpec = when(item) {
        Welcome -> WelcomeScreenDestination
        Tasks -> RecognitionTaskListScreenDestination
        FavoriteTasks -> FavoriteRecognitionTaskListScreenDestination
        Articles -> ArticlesScreenDestination
    }

    override fun navigateWelcomeScreen(from: DestinationSpec<*>) =
        initialNavigate(from, WelcomeScreenDestination)

    override fun navigateAuthScreen(from: DestinationSpec<*>) =
        initialNavigate(from, AuthScreenDestination)

    override fun navigateSettings() = navigate(SettingsScreenDestination)
    override fun navigateSolveTask(id: String)
        = navigate(SolveRecognitionTaskScreenDestination(id))
    override fun navigateAddTask() = navigate(AddRecognitionTaskScreenDestination)
}

@Composable
fun NavController.asDarkNavigator() = asNavigator<DarkNavigator>()
