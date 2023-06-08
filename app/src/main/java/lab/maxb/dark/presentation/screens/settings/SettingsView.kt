package lab.maxb.dark.presentation.screens.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavController
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.ramcosta.composedestinations.annotation.Destination
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import lab.maxb.dark.R
import lab.maxb.dark.presentation.components.ChooseLocaleDialog
import lab.maxb.dark.presentation.components.NavBackIcon
import lab.maxb.dark.presentation.components.ScaffoldWithDrawer
import lab.maxb.dark.presentation.components.TopBar
import lab.maxb.dark.presentation.components.getLanguageCode
import lab.maxb.dark.presentation.components.getLanguageName
import lab.maxb.dark.presentation.extra.ChangedEffect
import org.koin.androidx.compose.getViewModel


@Destination
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = getViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val onEvent = viewModel::onEvent

    uiState.localeUpdated.ChangedEffect(onConsumed = onEvent) {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(it.locale)
        )
    }

    ScaffoldWithDrawer(
        navController = navController,
        topBar = {
            TopBar(
                title = stringResource(id = R.string.nav_settings_title),
                navigationIcon = { NavBackIcon(navController = navController) },
            )
        }
    ) {
        SettingsRootStateless(uiState, onEvent)
    }
}

@Composable
fun SettingsRootStateless(
    uiState: SettingsUiState,
    onEvent: (SettingsUiEvent) -> Unit = {},
) {
    val chooseLocaleDialogState = rememberMaterialDialogState()
    val currentLanguage = getLanguageCode()

    Column(Modifier.fillMaxSize()) {
        SettingsMenuLink(
            icon = { Icon(painterResource(R.drawable.ic_language), null) },
            title = { Text(stringResource(R.string.settings_language_title)) },
            subtitle = {
                Text(
                    text = if (uiState.locale.isEmpty()) stringResource(
                        R.string.settings_language_auto,
                        currentLanguage
                    ) else getLanguageName(),
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            onClick = { chooseLocaleDialogState.show() },
        )
    }
    ChooseLocaleDialog(
        chooseLocaleDialogState,
        uiState.locale,
    ) {
        onEvent(SettingsUiEvent.LocaleChanged(it))
    }
}
