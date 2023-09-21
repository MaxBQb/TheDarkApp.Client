package lab.maxb.dark.ui.screens.auth.handle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
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
import lab.maxb.dark.ui.R
import lab.maxb.dark.ui.components.AnimateAppearance
import lab.maxb.dark.ui.components.LoadingCircle
import lab.maxb.dark.ui.components.LoadingComponent
import lab.maxb.dark.ui.components.LoadingError
import lab.maxb.dark.ui.extra.initialNavigate
import lab.maxb.dark.ui.extra.isSuccess
import lab.maxb.dark.ui.extra.require
import lab.maxb.dark.ui.screens.destinations.AuthHandleScreenDestination
import lab.maxb.dark.ui.screens.destinations.AuthScreenDestination
import lab.maxb.dark.ui.screens.destinations.WelcomeScreenDestination
import lab.maxb.dark.ui.theme.fontSize
import lab.maxb.dark.ui.theme.spacing
import lab.maxb.dark.ui.theme.units.sdp
import org.koin.androidx.compose.getViewModel
import lab.maxb.dark.ui.screens.auth.handle.AuthHandleUiContract as Ui


@RootNavGraph(start = true)
@Destination
@Composable
fun AuthHandleScreen(
    navigator: DestinationsNavigator,
    viewModel: AuthHandleViewModel = getViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val onEvent = viewModel::onEvent
    LoadingComponent(
        result = uiState.authorized,
        onLoading = { AuthHandleLoading() },
        onError = { AuthHandleError {
            onEvent(Ui.Event.Retry)
        } },
    ) 

    LaunchedEffect(uiState.authorized) {
        if (!uiState.authorized.isSuccess)
            return@LaunchedEffect
        val destination = if (uiState.authorized.require())
                WelcomeScreenDestination
            else AuthScreenDestination
        navigator.initialNavigate(destination, AuthHandleScreenDestination)
    }
}


@Preview
@Composable
fun AuthHandleLoading() = AnimateAppearance(Modifier.fillMaxSize()) {
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

@Preview
@Composable
fun AuthHandleError(
    onRetry: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(id = R.string.handleAuth_message_ProfileNotFound),
            modifier = Modifier.padding(MaterialTheme.spacing.normal),
            fontSize = MaterialTheme.fontSize.normalHeader
        )
        LoadingError(
            Modifier
                .size(200.sdp)
                .padding(MaterialTheme.spacing.extraSmall)
        )
        Button(onClick = onRetry) {
           Text(text = stringResource(R.string.handleAuth_retry))
        }
    }
}