package lab.maxb.dark.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun rememberSnackbarHostState() = remember { SnackbarHostState() }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldWithDrawer(
    navController: NavController,
    modifier: Modifier = Modifier,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    snackbarState: SnackbarHostState = rememberSnackbarHostState(),
    topBar: @Composable () -> Unit = {},
    content: @Composable BoxScope.() -> Unit = {},
) = Drawer(
    drawerState = drawerState,
    navController = navController,
) {
    AppScaffold(modifier, topBar, snackbarState, content)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    snackbarState: SnackbarHostState = rememberSnackbarHostState(),
    content: @Composable() (BoxScope.() -> Unit) = {}
) = Scaffold(
    modifier = modifier,
    topBar = topBar,
    snackbarHost = {
        SnackbarHost(snackbarState) {
            Snackbar(
                it,
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp),
                contentColor = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(it), content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopScaffold(
    navController: NavController,
    modifier: Modifier = Modifier,
    title: String = "",
    snackbarState: SnackbarHostState = rememberSnackbarHostState(),
    actions: @Composable() (RowScope.() -> Unit) = {},
    content: @Composable() (BoxScope.() -> Unit) = {},
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ScaffoldWithDrawer(
        modifier = modifier,
        drawerState = drawerState,
        navController = navController,
        snackbarState = snackbarState,
        topBar = {
            TopBar(title, navigationIcon = {
                IconButton(
                    onClick = { scope.launch { drawerState.open() } },
                    enabled = LocalConfiguration.current.orientation != Configuration.ORIENTATION_LANDSCAPE,
                ) {
                    Icon(Icons.Filled.Menu, contentDescription = "")
                }
            }, actions = actions)
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