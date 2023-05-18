package lab.maxb.dark.presentation.screens.welcome

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import lab.maxb.dark.R
import lab.maxb.dark.domain.model.Role
import lab.maxb.dark.domain.model.isUser
import lab.maxb.dark.presentation.components.LoadingComponent
import lab.maxb.dark.presentation.components.SettingsButton
import lab.maxb.dark.presentation.components.TopScaffold
import lab.maxb.dark.ui.theme.Golden
import lab.maxb.dark.ui.theme.fontSize
import lab.maxb.dark.ui.theme.spacing
import org.koin.androidx.compose.getViewModel


@Destination
@Composable
fun WelcomeScreen(
    navigator: DestinationsNavigator,
    navController: NavController,
    viewModel: WelcomeViewModel = getViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val onEvent = viewModel::onEvent
    TopScaffold(
        navController = navController,
        title = stringResource(R.string.nav_home_title),
        actions = { SettingsButton(navigator) }
    ) {
        LoadingComponent(result = uiState) {
            WelcomeRootStateless(it, onEvent)
        }
    }
}

@Composable
fun WelcomeRootStateless(
    uiState: WelcomeUiState,
    onEvent: (WelcomeUiEvent) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.normal),
    ) {
        Greeting(uiState.user?.name)
        RoleName(uiState.role)
        if (uiState.role.isUser)
            UserRating(uiState.user?.rating ?: 0)
    }
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        Exit { onEvent(WelcomeUiEvent.SignOut) }
    }
}

@Composable
fun Exit(modifier: Modifier = Modifier, onExit: () -> Unit) = Button(
    onClick = onExit,
    modifier = modifier.padding(MaterialTheme.spacing.normal)
) {
    Icon(
        Icons.Filled.ExitToApp,
        "",
        modifier=Modifier
            .size(ButtonDefaults.IconSize)
    )
    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
    Text(text = stringResource(id = R.string.auth_menu_signOut))
}

@Composable
fun UserRating(rating: Int) {
    Text(
        buildAnnotatedString {
            append(stringResource(id = R.string.welcome_rating))
            append(": ")
            withStyle(SpanStyle(Golden)) {
                append(rating.toString())
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}


@Composable
fun Greeting(name: String?) {
    Text(
        name?.let {
            stringResource(id = R.string.welcome_welcome, it)
        } ?: stringResource(id = R.string.welcome_anonymousWelcome),
        fontSize = MaterialTheme.fontSize.normalHeader,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
fun RoleName(role: Role) {
    Text(
        buildAnnotatedString {
            append(stringResource(id = R.string.welcome_role))
            append(": ")
            withStyle(SpanStyle(Color.LightGray)) {
                append(stringResource(id = when (role) {
                    Role.USER -> R.string.role_user
                    Role.PREMIUM_USER -> R.string.role_premium_user
                    Role.MODERATOR -> R.string.role_moderator
                    Role.ADMINISTRATOR -> R.string.role_admin
                    Role.CONSULTOR -> R.string.role_consultor
                }))
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}
