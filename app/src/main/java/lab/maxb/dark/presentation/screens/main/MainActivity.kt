package lab.maxb.dark.presentation.screens.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.os.LocaleListCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
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
import lab.maxb.dark.presentation.theme.DarkAppTheme
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.util.Locale


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        handleLocale()
        super.onCreate(savedInstanceState)
        setContent {
            DarkAppTheme {
                Surface {
                    MainRoot()
                }
            }
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
            val bottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            view.updatePadding(bottom = bottom)
            insets
        }
    }

    private fun handleLocale(
        viewModel: MainViewModel = getViewModel()
    ) {
        val locale = viewModel.getLocale(
            AppCompatDelegate.getApplicationLocales().toLanguageTags(),
            Locale.getDefault().toLanguageTag(),
        )
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(locale)
        )
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