package lab.maxb.dark.presentation.view

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import kotlinx.coroutines.launch
import lab.maxb.dark.R
import lab.maxb.dark.presentation.view.destinations.Destination
import lab.maxb.dark.presentation.view.destinations.RecognitionTaskListScreenDestination
import lab.maxb.dark.presentation.view.destinations.WelcomeScreenDestination


enum class DrawerDestination(
    val direction: DirectionDestinationSpec,
    @DrawableRes val icon: Int,
    @StringRes val label: Int
) {
    Welcome(WelcomeScreenDestination, R.drawable.ic_home, R.string.nav_main_label),
    Tasks(RecognitionTaskListScreenDestination, R.drawable.ic_list, R.string.nav_taskList_label),
}


val NavController.localDestination @Composable get(): Destination
    = appCurrentDestinationAsState().value
    ?: NavGraphs.root.startAppDestination


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
                    modifier = Modifier.fillMaxWidth().align(CenterHorizontally)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String = "",
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        navigationIcon = navigationIcon,
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        actions = actions,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldWithDrawer(
    navController: NavController,
    modifier: Modifier = Modifier,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    topBar: @Composable () -> Unit = {},
    content: @Composable BoxScope.() -> Unit = {},
) = Drawer(
    drawerState = drawerState,
    navController = navController,
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
    ) {
        Box(modifier=Modifier.fillMaxSize().padding(it), content=content)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopScaffold(
    navController: NavController,
    modifier: Modifier = Modifier,
    title: String = "",
    content: @Composable BoxScope.() -> Unit = {},
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ScaffoldWithDrawer(
        modifier = modifier,
        drawerState = drawerState,
        navController = navController,
        topBar = {
            TopBar(title, navigationIcon={
                IconButton(
                    onClick = { scope.launch { drawerState.open() } },
                    enabled = LocalConfiguration.current.orientation != ORIENTATION_LANDSCAPE,
                ) {
                    Icon(Icons.Filled.Menu, contentDescription = "")
                }
            })
        },
        content = content
    )
}

@Composable
fun NavBackIcon(navController: NavController) = IconButton(onClick = {
    navController.navigateUp()
}) {
    Icon(Icons.Filled.ArrowBack, contentDescription = "")
}

