package lab.maxb.dark.ui.screens.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SwitchDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavController
import com.alorma.compose.settings.storage.base.rememberBooleanSettingState
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.SettingsSwitch
import com.ramcosta.composedestinations.annotation.Destination
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import lab.maxb.dark.ui.components.ChooseLocaleDialog
import lab.maxb.dark.ui.components.NavBackIcon
import lab.maxb.dark.ui.components.ScaffoldWithDrawer
import lab.maxb.dark.ui.components.TopBar
import lab.maxb.dark.ui.components.getLanguageCode
import lab.maxb.dark.ui.components.getLanguageName
import lab.maxb.dark.ui.core.BuildConfig
import lab.maxb.dark.ui.screens.core.effects.SideEffect
import lab.maxb.dark.ui.settings.R
import lab.maxb.dark.ui.theme.spacing
import org.koin.androidx.compose.getViewModel
import lab.maxb.dark.ui.core.R as coreR
import lab.maxb.dark.ui.navigation.api.R as navR
import lab.maxb.dark.ui.screens.settings.SettingsUiContract as Ui


@Composable
fun getAppVersion() = with(LocalContext.current) {
    packageManager.getPackageInfo(packageName, 0).versionName ?: "0.0.0"
}

@Destination
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = getViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val onEvent = viewModel::onEvent

    SideEffect<Ui.SideEffect.LocaleUpdated>(
        uiState.sideEffectsHolder,
        { onEvent(Ui.Event.EffectConsumed(it)) },
        true,
    ) {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(it.locale)
        )
    }

    ScaffoldWithDrawer(
        navController = navController,
        topBar = {
            TopBar(
                title = stringResource(id = navR.string.nav_settings_title),
                navigationIcon = { NavBackIcon(navController = navController) },
            )
        }
    ) {
        SettingsRootStateless(uiState, onEvent)
    }
}

@Composable
fun SettingsRootStateless(
    uiState: Ui.State,
    onEvent: (Ui.Event) -> Unit = {},
) {
    val chooseLocaleDialogState = rememberMaterialDialogState()
    val currentLanguage = getLanguageCode()
    Column(
        Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            SettingsMenuLink(
                icon = { Icon(painterResource(coreR.drawable.ic_language), null) },
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
            val useExternalSuggestionsState = rememberBooleanSettingState()
            useExternalSuggestionsState.value = uiState.useExternalSuggestions
            SettingsSwitch(
                icon = { Icon(painterResource(R.drawable.ic_suggestions), contentDescription = null) },
                enabled = false, // TODO: Fix external suggestions service or remove it completely
                state = useExternalSuggestionsState,
                title = { Text(stringResource(R.string.settings_useExternalSuggestions)) },
                switchColors = SwitchDefaults.colors(
                    uncheckedThumbColor = androidx.compose.material.MaterialTheme.colors.primary,
                    checkedThumbColor = androidx.compose.material.MaterialTheme.colors.error,
                ),
                onCheckedChange = { onEvent(Ui.Event.UseExternalSuggestionsToggled) },
            )
        }
        val uriHandler = LocalUriHandler.current
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    uriHandler.openUri(BuildConfig.APP_REPOSITORY)
                },
        ) {
            Text(
                text = stringResource(
                    R.string.settings_version_info,
                    stringResource(coreR.string.app_name),
                    getAppVersion(),
                ),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSecondary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(MaterialTheme.spacing.normal),
            )
        }
    }
    ChooseLocaleDialog(
        chooseLocaleDialogState,
        uiState.locale,
    ) {
        onEvent(Ui.Event.LocaleChanged(it))
    }
}
