package lab.maxb.dark.ui.screens.main

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
import lab.maxb.dark.ui.AppNavigation
import lab.maxb.dark.ui.asDarkNavigator
import lab.maxb.dark.ui.extra.isSuccess
import lab.maxb.dark.ui.extra.require
import lab.maxb.dark.ui.screens.destinations.AuthHandleScreenDestination
import lab.maxb.dark.ui.screens.destinations.AuthScreenDestination
import lab.maxb.dark.ui.theme.DarkAppTheme
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import java.util.Locale


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        handleLocale()
        super.onCreate(savedInstanceState)
        setContent {
            DarkAppTheme {
                Surface {
                    KoinAndroidContext {
                        MainRoot()
                    }
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
        viewModel: MainViewModel = getViewModel(),
    ) {
        navController = rememberNavController()
        AppNavigation(navController as NavHostController)
        val navigator = navController.asDarkNavigator()
        val uiState by viewModel.uiState.collectAsState()
        val currentDestination = navigator.currentDestination
        LaunchedEffect(uiState.authorized) {
            if (!uiState.authorized.isSuccess
                || uiState.authorized.require()
                || currentDestination == AuthHandleScreenDestination
                || currentDestination == AuthScreenDestination)
                return@LaunchedEffect
            navigator.initialNavigate(currentDestination, AuthHandleScreenDestination)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navController.handleDeepLink(intent)
    }
}