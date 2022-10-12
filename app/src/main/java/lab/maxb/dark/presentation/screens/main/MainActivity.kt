package lab.maxb.dark.presentation.screens.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import lab.maxb.dark.presentation.extra.initialNavigate
import lab.maxb.dark.presentation.extra.isSuccess
import lab.maxb.dark.presentation.extra.localDestination
import lab.maxb.dark.presentation.extra.require
import lab.maxb.dark.presentation.screens.NavGraphs
import lab.maxb.dark.presentation.screens.destinations.AuthHandleScreenDestination
import lab.maxb.dark.presentation.screens.destinations.AuthScreenDestination
import lab.maxb.dark.ui.theme.DarkAppTheme
import org.koin.androidx.viewmodel.ext.android.getViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DarkAppTheme {
                Surface {
                    MainRoot()
                }
            }
        }
    }

    @Composable
    private fun MainRoot(
        viewModel: MainViewModel = getViewModel()
    ) {
        navController = rememberNavController()
        DestinationsNavHost(
            navGraph = NavGraphs.root,
            navController = navController as NavHostController,
        )
        val uiState by viewModel.uiState.collectAsState()
        val currentDestination = navController.localDestination
        LaunchedEffect(uiState.authorized) {
            if (!uiState.authorized.isSuccess
                || uiState.authorized.require()
                || currentDestination == AuthHandleScreenDestination
                || currentDestination == AuthScreenDestination)
                return@LaunchedEffect
            navController.initialNavigate(AuthHandleScreenDestination, currentDestination)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navController.handleDeepLink(intent)
    }
}