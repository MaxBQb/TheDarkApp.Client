package lab.maxb.dark.presentation.screens.auth.form

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.core.os.LocaleListCompat
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import lab.maxb.dark.R
import lab.maxb.dark.presentation.components.AnimatedScaleToggle
import lab.maxb.dark.presentation.components.AppScaffold
import lab.maxb.dark.presentation.components.ChooseLocaleDialog
import lab.maxb.dark.presentation.components.LabelledSwitch
import lab.maxb.dark.presentation.components.LanguageToggleButton
import lab.maxb.dark.presentation.components.LoadingScreen
import lab.maxb.dark.presentation.components.rememberSnackbarHostState
import lab.maxb.dark.presentation.components.utils.InputOptions
import lab.maxb.dark.presentation.components.utils.getPasswordTransformation
import lab.maxb.dark.presentation.components.utils.keyboardClose
import lab.maxb.dark.presentation.components.utils.keyboardNext
import lab.maxb.dark.presentation.components.utils.withInputOptions
import lab.maxb.dark.presentation.extra.ChangedEffect
import lab.maxb.dark.presentation.extra.initialNavigate
import lab.maxb.dark.presentation.extra.show
import lab.maxb.dark.presentation.screens.destinations.AuthScreenDestination
import lab.maxb.dark.presentation.screens.destinations.WelcomeScreenDestination
import lab.maxb.dark.ui.theme.DarkAppTheme
import lab.maxb.dark.ui.theme.LocalAnimationDurations
import lab.maxb.dark.ui.theme.animationDurations
import lab.maxb.dark.ui.theme.spacing
import lab.maxb.dark.ui.theme.units.sdp
import org.koin.androidx.compose.getViewModel


@Destination
@Composable
fun AuthScreen(
    navigator: DestinationsNavigator,
    viewModel: AuthViewModel = getViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val onEvent = viewModel::onEvent
    val snackbarState = rememberSnackbarHostState()

    AppScaffold(snackbarState = snackbarState) {
        CompositionLocalProvider(
            LocalAnimationDurations provides MaterialTheme.animationDurations.let {
                it.copy(default=it.long)
            }
        ) {
            AuthRootStateless(
                uiState = uiState,
                onEvent = onEvent,
            )
        }
    }

    uiState.errors.ChangedEffect(snackbarState, onConsumed = onEvent) {
        snackbarState show it.message
    }
    uiState.authorized.ChangedEffect(onConsumed = onEvent) {
        navigator.initialNavigate(WelcomeScreenDestination, AuthScreenDestination)
    }
    uiState.localeUpdated.ChangedEffect(onConsumed = onEvent) {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(it.locale)
        )
    }
}

@Composable
fun AuthRootPreview(uiState: AuthUiState) = DarkAppTheme {
    Surface {
        AuthRootStateless(uiState)
    }
}

private class AuthUiStatePreviewParameterProvider : PreviewParameterProvider<AuthUiState> {
    override val values = sequenceOf(
        AuthUiState(
            isAccountNew = false,
        ),
        AuthUiState(
            isAccountNew = true,
        ),
        AuthUiState(
            isLoading = true,
        ),
    )
}

@Preview
@Composable
fun AuthRootPreviewAll(
    @PreviewParameter(AuthUiStatePreviewParameterProvider::class) state: AuthUiState
) = AuthRootPreview(state)


@Composable
fun AuthRootStateless(
    uiState: AuthUiState,
    onEvent: ((AuthUiEvent) -> Unit) = {},
) {
    val localFocus = LocalFocusManager.current
    LaunchedEffect(true) {
        localFocus.clearFocus(true)
    }
    val dialogState = rememberMaterialDialogState()

    Column(
        modifier = Modifier
            .padding(
                MaterialTheme.spacing.normal,
                MaterialTheme.spacing.zero,
            )
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = MaterialTheme.spacing.small),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedScaleToggle(uiState.isAccountNew) {
                Text(
                    stringResource(
                        if (it)
                            R.string.auth_signup_label
                        else R.string.auth_login_label
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.normal),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                )
            }
            LanguageToggleButton { dialogState.show() }
        }

        val padding = Modifier
            .padding(
                MaterialTheme.spacing.zero,
                MaterialTheme.spacing.small,
            )
            .widthIn(min = 220.sdp)

        Column {
            LabelledSwitch(
                checked = uiState.isAccountNew,
                onCheckedChange = {
                    onEvent(AuthUiEvent.RegistrationNeededChanged(it))
                },
                label = stringResource(R.string.auth_isAccountNew),
                modifier = padding,
            )
            OutlinedTextField(
                value = uiState.login,
                onValueChange = {
                    onEvent(AuthUiEvent.LoginChanged(it))
                },
                label = { Text(stringResource(R.string.auth_loginHint)) },
                modifier = padding.withInputOptions(keyboardNext),
                keyboardOptions = keyboardNext.options,
                keyboardActions = keyboardNext.actions,
                singleLine = true,
            )
            InputPasswordTwice(
                modifier = padding,
                password = uiState.password,
                onPasswordChanged = {
                    onEvent(AuthUiEvent.PasswordChanged(it))
                },
                showPassword = uiState.showPassword,
                onPasswordVisibilityChange = {
                    onEvent(AuthUiEvent.PasswordVisibilityChanged(it))
                },
                repeatRequired = uiState.isAccountNew,
                passwordRepeat = uiState.passwordRepeat,
                onPasswordRepeatChange = {
                    onEvent(AuthUiEvent.PasswordRepeatChanged(it))
                },
            )
        }
        AuthButton(uiState.isAccountNew) {
            onEvent(AuthUiEvent.Submit)
        }
    }

    ChooseLocaleDialog(
        dialogState,
        uiState.locale,
    ) {
        onEvent(AuthUiEvent.LocaleChanged(it))
    }
    LoadingScreen(uiState.isLoading)
}

@Composable
private fun ColumnScope.InputPasswordTwice(
    modifier: Modifier,
    password: String,
    onPasswordChanged: (String) -> Unit,
    showPassword: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    repeatRequired: Boolean,
    passwordRepeat: String,
    onPasswordRepeatChange: (String) -> Unit,
    appearanceDuration: Int = LocalAnimationDurations.current.default,
) {
    InputPassword(
        value = password,
        modifier = modifier,
        onValueChange = onPasswordChanged,
        label = { Text(stringResource(R.string.auth_passwordHint)) },
        showPassword = showPassword,
        onPasswordVisibilityChange = onPasswordVisibilityChange,
        options = if (repeatRequired) keyboardNext
        else keyboardClose
    )
    AnimatedVisibility(
        repeatRequired,
        enter = fadeIn(tween(appearanceDuration)) + expandVertically(tween(appearanceDuration)),
        exit = fadeOut(tween(appearanceDuration)) + shrinkVertically(tween(appearanceDuration))
    ) {
        InputPassword(
            value = passwordRepeat,
            onValueChange = onPasswordRepeatChange,
            label = { Text(stringResource(R.string.auth_passwordRepeatHint)) },
            modifier = modifier,
            options = keyboardClose,
            showPassword = showPassword,
            onPasswordVisibilityChange = onPasswordVisibilityChange,
        )
    }
}

@Composable
private fun InputPassword(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    label: @Composable (() -> Unit),
    showPassword: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    options: InputOptions,
) = OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    label = label,
    visualTransformation = getPasswordTransformation(showPassword),
    modifier = modifier.withInputOptions(options),
    trailingIcon = {
        ShowPasswordIcon(showPassword, onPasswordVisibilityChange)
    },
    singleLine = true,
    keyboardOptions = options.options,
    keyboardActions = options.actions,
)

@Composable
private fun AuthButton(
    isAccountNew: Boolean,
    onAuthClick: () -> Unit,
) = Button(
    modifier = Modifier
        .padding(
            MaterialTheme.spacing.large,
            MaterialTheme.spacing.small,
        )
        .fillMaxWidth(),
    onClick = onAuthClick
) {
    AnimatedScaleToggle(isAccountNew) {
        Text(
            stringResource(
                if (it) R.string.auth_signup_button
                else R.string.auth_login_button
            ),
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun ShowPasswordIcon(
    showPassword: Boolean,
    onChanged: (Boolean) -> Unit,
) = IconButton({ onChanged(!showPassword) }) {
    AnimatedScaleToggle(value = showPassword) {
        Icon(
            painterResource(
                if (it)
                    R.drawable.ic_visible
                else
                    R.drawable.ic_invisible
            ),
            null,
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}
