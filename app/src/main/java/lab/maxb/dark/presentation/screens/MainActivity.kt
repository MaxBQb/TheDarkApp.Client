package lab.maxb.dark.presentation.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.popUpTo
import lab.maxb.dark.presentation.extra.localDestination
import lab.maxb.dark.presentation.screens.auth.AuthViewModel
import lab.maxb.dark.presentation.screens.destinations.AuthHandleScreenDestination
import lab.maxb.dark.presentation.screens.destinations.WelcomeScreenDestination
import lab.maxb.dark.ui.theme.DarkAppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private val authViewModel: AuthViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DarkAppTheme {
                Surface {
                    navController = rememberNavController()
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        navController = navController as NavHostController,
                    )
                    val profile by authViewModel.profile.collectAsState()
                    val currentDestination = navController.localDestination
                    LaunchedEffect(profile) {
                        profile.ifLoaded { it ->
                            if (it == null
                                && currentDestination != AuthHandleScreenDestination
                            )
                                navController.navigate(AuthHandleScreenDestination.route) {
                                    launchSingleTop = true
                                    popUpTo(WelcomeScreenDestination) {
                                        inclusive = true
                                    }
                                }
                            authViewModel.handleAuthorizedStateChanges()
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navController.handleDeepLink(intent)
    }
}