package lab.maxb.dark.presentation.screens.welcome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import lab.maxb.dark.R
import lab.maxb.dark.domain.model.Role
import lab.maxb.dark.domain.model.User
import lab.maxb.dark.domain.model.isUser
import lab.maxb.dark.presentation.components.LoadingCircle
import lab.maxb.dark.presentation.components.LoadingComponent
import lab.maxb.dark.presentation.components.SettingsButton
import lab.maxb.dark.presentation.components.TopScaffold
import lab.maxb.dark.ui.theme.DarkAppTheme
import lab.maxb.dark.ui.theme.Golden
import lab.maxb.dark.ui.theme.fontSize
import lab.maxb.dark.ui.theme.spacing
import lab.maxb.dark.ui.theme.units.sdp
import org.koin.androidx.compose.getViewModel
import lab.maxb.dark.presentation.screens.welcome.WelcomeUiContract as Ui


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
        LoadingComponent(result = uiState, onLoading = {
            LoadingCircle(
                width = 8,
                modifier = Modifier
                    .alpha(0.5f)
                    .padding(32.sdp)
            )
        }) {
            WelcomeRootStateless(it, onEvent)
        }
    }
}

@Composable
fun WelcomeRootStateless(
    uiState: Ui.State,
    onEvent: (Ui.Event) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.normal),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Greeting(uiState.user?.name)
            RoleName(uiState.role)
            if (uiState.role?.isUser == true)
                UserRating(uiState.user?.rating ?: 0)
            AnimatedVisibility(
                uiState.dailyArticle != null
                        && uiState.role?.isUser == true
            ) {
                DailyArticle(uiState.dailyArticle ?: "")
            }
        }
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Exit { onEvent(Ui.Event.SignOut) }
            }
        }
    }
}

@Composable
private fun DailyArticle(text: String) {
    Column(modifier = Modifier.padding(top = MaterialTheme.spacing.large)) {
        Text(
            text = stringResource(R.string.welcome_dailyArticle_title),
            fontWeight = FontWeight.Bold,
        )
        Divider()
        val scrollableState = rememberScrollState(0)
        Text(
            modifier = Modifier
                .height((24 * 8).sdp)
                .verticalScroll(scrollableState),
            text = text,
            textAlign = TextAlign.Justify,
        )
    }
}

@Composable
fun Exit(modifier: Modifier = Modifier, onExit: () -> Unit) = Button(
    onClick = onExit,
    modifier = modifier.padding(horizontal = MaterialTheme.spacing.normal)
) {
    Icon(
        Icons.Filled.ExitToApp,
        "",
        modifier = Modifier
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = MaterialTheme.spacing.extraSmall,
                bottom = MaterialTheme.spacing.normal,
            ),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun RoleName(role: Role?) = AnimatedVisibility(role != null) {
    Text(
        buildAnnotatedString {
            append(stringResource(id = R.string.welcome_role))
            append(": ")
            withStyle(SpanStyle(Color.LightGray)) {
                append(
                    stringResource(
                        when (role ?: Role.USER) {
                            Role.USER -> R.string.role_user
                            Role.PREMIUM_USER -> R.string.role_premium_user
                            Role.MODERATOR -> R.string.role_moderator
                            Role.ADMINISTRATOR -> R.string.role_admin
                            Role.CONSULTOR -> R.string.role_consultor
                        }
                    )
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}


@Preview
@Composable
fun WelcomePreview() = DarkAppTheme {
    Surface {
        WelcomeRootStateless(
            Ui.State(
                User("TesterName", 42),
                Role.USER,
                "Hello here is some article text for you! ".repeat(80)
            )
        )
    }
}