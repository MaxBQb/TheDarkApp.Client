package lab.maxb.dark.presentation.screens.auth.form

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import lab.maxb.dark.R
import lab.maxb.dark.presentation.components.AppScaffold
import lab.maxb.dark.presentation.components.LabelledSwitch
import lab.maxb.dark.presentation.components.LoadingScreen
import lab.maxb.dark.presentation.components.rememberSnackbarHostState
import lab.maxb.dark.presentation.components.utils.getPasswordTransformation
import lab.maxb.dark.presentation.components.utils.keyboardClose
import lab.maxb.dark.presentation.components.utils.keyboardNext
import lab.maxb.dark.presentation.extra.ChangedEffect
import lab.maxb.dark.presentation.extra.initialNavigate
import lab.maxb.dark.presentation.extra.show
import lab.maxb.dark.presentation.screens.destinations.AuthScreenDestination
import lab.maxb.dark.presentation.screens.destinations.WelcomeScreenDestination
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

    AppScaffold(snackbarState=snackbarState) {
        AuthRootStateless(
            uiState = uiState,
            onEvent = onEvent,
        )
    }

    uiState.errors.ChangedEffect(snackbarState, onConsumed = onEvent) {
        snackbarState show it.message
    }
    uiState.authorized.ChangedEffect(onConsumed = onEvent) {
        navigator.initialNavigate(WelcomeScreenDestination(), AuthScreenDestination)
    }
}

@Preview
@Composable
fun AuthRootPreviewLogin() = AuthRootStateless(
    AuthUiState(
        isAccountNew = false,
    ),
)

@Preview
@Composable
fun AuthRootPreviewSignup() = AuthRootStateless(
    AuthUiState(
        isAccountNew = true,
    ),
)

@Preview
@Composable
fun AuthRootPreviewLoading() = AuthRootStateless(
    AuthUiState(
        isLoading = true,
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthRootStateless(
    uiState: AuthUiState,
    onEvent: ((AuthUiEvent) -> Unit) = {},
) {
    val localFocus = LocalFocusManager.current
    LaunchedEffect(true) {
        localFocus.clearFocus(true)
    }

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
        Text(
            stringResource(
                if (uiState.isAccountNew)
                    R.string.auth_signup_label
                else R.string.auth_login_label
            ),
            modifier = Modifier.padding(MaterialTheme.spacing.normal),
            style = MaterialTheme.typography.titleLarge
        )
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
                    modifier = padding.onPreviewKeyEvent(keyboardNext.event),
                    keyboardOptions = keyboardNext.options,
                    keyboardActions = keyboardNext.actions,
                    maxLines = 1,
                    singleLine = true,
                )
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = {
                        onEvent(AuthUiEvent.PasswordChanged(it))
                    },
                    label = { Text(stringResource(R.string.auth_passwordHint)) },
                    visualTransformation = getPasswordTransformation(uiState.showPassword),
                    modifier = if (uiState.isAccountNew)
                        padding.onPreviewKeyEvent(keyboardNext.event)
                    else padding.onPreviewKeyEvent(keyboardClose.event),
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = if (uiState.isAccountNew) keyboardNext.options
                        else keyboardClose.options,
                    keyboardActions = if (uiState.isAccountNew) keyboardNext.actions
                        else keyboardClose.actions,
                )
                AnimatedVisibility(uiState.isAccountNew) {
                    OutlinedTextField(
                        value = uiState.passwordRepeat,
                        onValueChange = {
                            onEvent(AuthUiEvent.PasswordRepeatChanged(it))
                        },
                        label = { Text(stringResource(R.string.auth_passwordRepeatHint)) },
                        visualTransformation = getPasswordTransformation(uiState.showPassword),
                        modifier = padding.onPreviewKeyEvent(keyboardClose.event),
                        maxLines = 1,
                        singleLine = true,
                        keyboardOptions = keyboardClose.options,
                        keyboardActions = keyboardClose.actions
                    )
                }
                LabelledSwitch(
                    checked = uiState.showPassword,
                    onCheckedChange = {
                        onEvent(AuthUiEvent.PasswordVisibilityChanged(it))
                    },
                    label = stringResource(R.string.auth_showPassword),
                    modifier = padding,
                )
            }
            Button(
                modifier = Modifier
                    .padding(
                        MaterialTheme.spacing.large,
                        MaterialTheme.spacing.small,
                    )
                    .fillMaxWidth(),
                onClick = {
                    onEvent(AuthUiEvent.Submit)
                }
            ) {
                Text(
                    stringResource(
                        if (uiState.isAccountNew)
                            R.string.auth_signup_button
                        else R.string.auth_login_button
                    ),
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    LoadingScreen(uiState.isLoading)
}
