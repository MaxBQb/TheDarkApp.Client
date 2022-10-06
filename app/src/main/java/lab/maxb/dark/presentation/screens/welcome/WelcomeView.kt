package lab.maxb.dark.presentation.screens.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import lab.maxb.dark.R
import lab.maxb.dark.presentation.components.LoadingComponent
import lab.maxb.dark.presentation.components.TopScaffold
import lab.maxb.dark.ui.theme.Golden
import lab.maxb.dark.ui.theme.fontSize
import lab.maxb.dark.ui.theme.spacing
import org.koin.androidx.compose.getViewModel


@Destination
@Composable
fun WelcomeScreen(
    navController: NavController,
    viewModel: WelcomeViewModel = getViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val onEvent = viewModel::onEvent
    TopScaffold(
        title = stringResource(R.string.nav_main_label),
        navController = navController,
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
        if (uiState.isUser)
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
