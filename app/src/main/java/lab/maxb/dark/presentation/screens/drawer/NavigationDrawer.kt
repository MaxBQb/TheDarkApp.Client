package lab.maxb.dark.presentation.screens.drawer

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import lab.maxb.dark.R
import lab.maxb.dark.presentation.extra.localDestination
import lab.maxb.dark.presentation.screens.destinations.ArticlesScreenDestination
import lab.maxb.dark.presentation.screens.destinations.FavoriteRecognitionTaskListScreenDestination
import lab.maxb.dark.presentation.screens.destinations.RecognitionTaskListScreenDestination
import lab.maxb.dark.presentation.screens.destinations.WelcomeScreenDestination
import org.koin.androidx.compose.getViewModel


enum class DrawerDestination(
    val direction: DirectionDestinationSpec,
    @DrawableRes val icon: Int,
    @StringRes val label: Int
) {
    Welcome(WelcomeScreenDestination, R.drawable.ic_home, R.string.nav_home_title),
    Tasks(RecognitionTaskListScreenDestination, R.drawable.ic_list, R.string.nav_taskList_title),
    FavoriteTasks(
        FavoriteRecognitionTaskListScreenDestination,
        R.drawable.ic_favorite,
        R.string.nav_favoriteTaskList_title
    ),
    Articles(ArticlesScreenDestination, R.drawable.ic_articles, R.string.nav_articles_title),
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun Drawer(
    navController: NavController,
    modifier: Modifier = Modifier,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    content: @Composable () -> Unit = {},
) {
    val viewModel: DrawerViewModel = getViewModel()
    val currentDestination = navController.localDestination
    if (LocalConfiguration.current.orientation == ORIENTATION_LANDSCAPE)
        return content()
    val uiState by viewModel.uiState.collectAsState()
    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.fillMaxWidth(0.75f)) {
                Image(
                    painterResource(R.drawable.ic_launcher_foreground),
                    null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(CenterHorizontally)
                )
                LazyColumn {
                    items(uiState.destinations) { destination ->
                        NavigationDrawerItem(
                            label = {
                                Text(
                                    stringResource(destination.label),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            },
                            selected = currentDestination == destination.direction,
                            onClick = {
                                navController.navigate(destination.direction) {
                                    launchSingleTop = true
                                }
                            },
                            icon = {
                                Icon(
                                    painterResource(destination.icon),
                                    stringResource(destination.label)
                                )
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                .animateItemPlacement()
                        )
                    }
                }
            }
        }, content = content
    )
}


