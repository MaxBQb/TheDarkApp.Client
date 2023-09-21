package lab.maxb.dark.ui.screens.task.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import lab.maxb.dark.ui.R
import lab.maxb.dark.ui.components.TopScaffold
import lab.maxb.dark.ui.screens.destinations.SolveRecognitionTaskScreenDestination
import org.koin.androidx.compose.getViewModel


@Destination
@Composable
fun FavoriteRecognitionTaskListScreen(
    navigator: DestinationsNavigator,
    navController: NavController,
    viewModel: RecognitionTaskListViewModel = getViewModel(),
) = TopScaffold(
    navController = navController,
    title = stringResource(id = R.string.nav_favoriteTaskList_title),
) {
    val hasFavorites by viewModel.hasFavorites.collectAsState()
    LaunchedEffect(hasFavorites) {
        if (!hasFavorites)
            navController.navigateUp()
    }
    val items = viewModel.favoriteRecognitionTaskList.collectAsLazyPagingItems()

    RecognitionTaskList(
        items,
        onItemClick = {
            navigator.navigate(SolveRecognitionTaskScreenDestination(it))
        },
        resolveImage = viewModel::getImage,
        true,
        viewModel::markFavorite,
    )
}
