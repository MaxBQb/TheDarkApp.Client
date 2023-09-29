package lab.maxb.dark.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import lab.maxb.dark.ui.screens.drawer.Drawer

@Composable
fun rememberSnackbarHostState() = remember { SnackbarHostState() }


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


@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    snackbarState: SnackbarHostState = rememberSnackbarHostState(),
    content: @Composable (BoxScope.() -> Unit) = {}
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


@Composable
fun TopScaffold(
    navController: NavController,
    modifier: Modifier = Modifier,
    title: String = "",
    snackbarState: SnackbarHostState = rememberSnackbarHostState(),
    actions: @Composable (RowScope.() -> Unit) = {},
    content: @Composable (BoxScope.() -> Unit) = {},
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
fun NavBackIcon(navController: NavController) = NavBackIcon {
    navController.navigateUp()
}

@Composable
fun NavBackIcon(onClick: () -> Unit) = IconButton(onClick = onClick) {
    Icon(Icons.Filled.ArrowBack, contentDescription = "")
}