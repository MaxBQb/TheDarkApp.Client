package lab.maxb.dark.presentation.view

import android.view.KeyEvent.ACTION_DOWN
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import lab.maxb.dark.R
import lab.maxb.dark.presentation.extra.ChangedEffect
import lab.maxb.dark.presentation.extra.show
import lab.maxb.dark.presentation.view.destinations.AuthScreenDestination
import lab.maxb.dark.presentation.view.destinations.WelcomeScreenDestination
import lab.maxb.dark.presentation.viewModel.AuthUiEvent
import lab.maxb.dark.presentation.viewModel.AuthUiState
import lab.maxb.dark.presentation.viewModel.AuthViewModel
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
    val context = LocalContext.current.applicationContext

    AuthRootStateless(
        uiState = uiState,
        onEvent = onEvent,
    )

    uiState.errors.ChangedEffect(onConsumed = onEvent) { // TODO: Use scaffoldState
        it.message.show(context) // TODO: replace with toast (snackbar)
    }
    uiState.authorized.ChangedEffect(onConsumed = onEvent) {
        navigator.navigate(WelcomeScreenDestination()) {
            launchSingleTop = true
            popUpTo(AuthScreenDestination) {
                inclusive = true
            }
        }
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


@Composable
fun LabelledSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit),
    label: String,
    modifier: Modifier = Modifier,
    switchModifier: Modifier = Modifier,
    labelModifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .clickable(
                indication = rememberRipple(color = MaterialTheme.colorScheme.primary),
                interactionSource = remember { MutableInteractionSource() },
                onClick = { onCheckedChange(!checked) }
            )
            .requiredHeight(ButtonDefaults.MinHeight)
    ) {
        Switch(
            checked = checked,
            onCheckedChange = null,
            modifier = switchModifier,
        )
        Spacer(Modifier.size(6.sdp))
        Text(
            text = label,
            modifier = labelModifier,
        )
    }
}

@Composable
fun LoadingScreen(show: Boolean, alpha: Float = 0.7f) = AnimatedVisibility(
    visible = show,
    enter = fadeIn(),
    exit = fadeOut(),
) {
    Box(
        Modifier
            .fillMaxSize()
            .alpha(alpha)
            .background(MaterialTheme.colorScheme.surface),
        Alignment.Center
    ) {
        LoadingCircle(
            width = 14,
            modifier = Modifier
                .alpha(alpha)
                .size(200.sdp)
        )
    }
}

fun getPasswordTransformation(showPassword: Boolean = false) = if (showPassword)
    VisualTransformation.None
else
    PasswordVisualTransformation()


data class InputOptions(
    val actions: KeyboardActions,
    val options: KeyboardOptions,
    val event: (KeyEvent) -> Boolean,
)

@OptIn(ExperimentalComposeUiApi::class)
private val goKeys = listOf(Key.Tab, Key.Enter)


val keyboardNext: InputOptions @Composable get() {
    val localFocus = LocalFocusManager.current
    return InputOptions(
        actions = KeyboardActions(onNext = {
            localFocus.moveFocus(FocusDirection.Down)
        }),
        options = KeyboardOptions(imeAction = ImeAction.Next),
        event = {
            if (it.key in goKeys && it.nativeKeyEvent.action == ACTION_DOWN){
                localFocus.moveFocus(FocusDirection.Down)
                true
            } else false
        }
    )
}


val keyboardClose: InputOptions @Composable get() {
    val localFocus = LocalFocusManager.current
    return InputOptions(
        actions = KeyboardActions(onDone = {
            localFocus.clearFocus()
        }),
        options = KeyboardOptions(imeAction = ImeAction.Done),
        event = {
            if (it.key in goKeys && it.nativeKeyEvent.action == ACTION_DOWN){
                localFocus.clearFocus()
                true
            } else false
        }
    )
}
