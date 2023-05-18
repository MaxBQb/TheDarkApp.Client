package lab.maxb.dark.presentation.components

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import lab.maxb.dark.presentation.screens.destinations.RecognitionTaskListScreenDestination
import lab.maxb.dark.presentation.screens.destinations.WelcomeScreenDestination


enum class DrawerDestination(
    val direction: DirectionDestinationSpec,
    @DrawableRes val icon: Int,
    @StringRes val label: Int
) {
    Welcome(WelcomeScreenDestination, R.drawable.ic_home, R.string.nav_home_title),
    Tasks(RecognitionTaskListScreenDestination, R.drawable.ic_list, R.string.nav_taskList_title),
    Articles(ArticlesScreenDestination, R.drawable.ic_articles, R.string.nav_articles_title),
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Drawer(
    navController: NavController,
    modifier: Modifier = Modifier,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    content: @Composable () -> Unit = {},
) {
    val currentDestination = navController.localDestination
    if (LocalConfiguration.current.orientation == ORIENTATION_LANDSCAPE)
        return content()
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
                DrawerDestination.values().forEach { destination ->
                    NavigationDrawerItem(
                        label = {
                            Text(stringResource(destination.label),
                                 color = MaterialTheme.colorScheme.onPrimary)
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
                    )
                }
            }
        }, content = content
    )
}


