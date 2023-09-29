package lab.maxb.dark.ui.screens.tasks_list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.ramcosta.composedestinations.annotation.Destination
import lab.maxb.dark.ui.components.TopScaffold
import lab.maxb.dark.ui.model.DrawerDestination
import org.koin.androidx.compose.getViewModel


@Destination
@Composable
fun FavoriteRecognitionTaskListScreen(
    navigator: TaskListNavigator,
    navController: NavController,
    viewModel: RecognitionTaskListViewModel = getViewModel(),
) = TopScaffold(
    navController = navController,
    title = stringResource(DrawerDestination.FavoriteTasks.label),
) {
    val hasFavorites by viewModel.hasFavorites.collectAsState()
    LaunchedEffect(hasFavorites) {
        if (!hasFavorites)
            navController.navigateUp()
    }
    val items = viewModel.favoriteRecognitionTaskList.collectAsLazyPagingItems()

    RecognitionTaskList(
        items,
        onItemClick = { navigator.navigateSolveTask(it) },
        resolveImage = viewModel::getImage,
        true,
        viewModel::markFavorite,
    )
}
