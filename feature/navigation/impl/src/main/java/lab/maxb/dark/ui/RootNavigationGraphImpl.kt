package lab.maxb.dark.ui

import com.ramcosta.composedestinations.spec.NavGraphSpec
import lab.maxb.dark.ui.screens.add_task.destinations.AddRecognitionTaskScreenDestination
import lab.maxb.dark.ui.screens.articles.destinations.ArticlesScreenDestination
import lab.maxb.dark.ui.screens.destinations.AuthHandleScreenDestination
import lab.maxb.dark.ui.screens.destinations.AuthScreenDestination
import lab.maxb.dark.ui.screens.settings.destinations.SettingsScreenDestination
import lab.maxb.dark.ui.screens.solve_task.destinations.SolveRecognitionTaskScreenDestination
import lab.maxb.dark.ui.screens.tasks_list.destinations.FavoriteRecognitionTaskListScreenDestination
import lab.maxb.dark.ui.screens.tasks_list.destinations.RecognitionTaskListScreenDestination
import lab.maxb.dark.ui.screens.welcome.destinations.WelcomeScreenDestination
import org.koin.core.annotation.Singleton

@Singleton
class RootNavigationGraphImpl : RootNavigationGraph {
    override val route = "root"
    override val startRoute = AuthHandleScreenDestination
    override val destinationsByRoute = listOf(
        ArticlesScreenDestination,
        WelcomeScreenDestination,
        SettingsScreenDestination,
        AuthScreenDestination,
        AuthHandleScreenDestination,
        RecognitionTaskListScreenDestination,
        FavoriteRecognitionTaskListScreenDestination,
        AddRecognitionTaskScreenDestination,
        SolveRecognitionTaskScreenDestination,
    ).associateBy { it.route }
    override val nestedNavGraphs = emptyList<NavGraphSpec>()
}