package lab.maxb.dark.presentation.screens.auth.handle

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import lab.maxb.dark.R
import lab.maxb.dark.presentation.components.LoadingCircle
import lab.maxb.dark.presentation.extra.isLoading
import lab.maxb.dark.presentation.extra.valueOrNull
import lab.maxb.dark.presentation.screens.destinations.AuthHandleScreenDestination
import lab.maxb.dark.presentation.screens.destinations.AuthScreenDestination
import lab.maxb.dark.presentation.screens.destinations.WelcomeScreenDestination
import lab.maxb.dark.ui.theme.fontSize
import lab.maxb.dark.ui.theme.spacing
import lab.maxb.dark.ui.theme.units.sdp
import org.koin.androidx.compose.getViewModel


@RootNavGraph(start = true)
@Destination
@Composable
fun AuthHandleScreen(
    navigator: DestinationsNavigator,
    viewModel: AuthHandleViewModel = getViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    AuthHandleRootStateless()

    LaunchedEffect(uiState.authorized) {
        if (uiState.authorized.isLoading)
            return@LaunchedEffect
        val destination = if (uiState.authorized.valueOrNull == true)
                WelcomeScreenDestination
            else AuthScreenDestination
        navigator.navigate(destination) {
            launchSingleTop = true
            popUpTo(AuthHandleScreenDestination) {
                inclusive = true
            }
        }
    }
}


@Preview
@Composable
fun AuthHandleRootStateless() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(id = R.string.handleAuth_pleaseWait),
            modifier = Modifier.padding(MaterialTheme.spacing.normal),
            fontSize = MaterialTheme.fontSize.normalHeader
        )
        LoadingCircle(modifier = Modifier.size(200.sdp), width = 12)
    }
}