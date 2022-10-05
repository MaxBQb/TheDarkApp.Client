package lab.maxb.dark.presentation.screens.task.solve

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.FULL_ROUTE_PLACEHOLDER
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import lab.maxb.dark.R
import lab.maxb.dark.domain.operations.shareLink
import lab.maxb.dark.presentation.components.*
import lab.maxb.dark.presentation.extra.ChangedEffect
import lab.maxb.dark.presentation.extra.show
import lab.maxb.dark.presentation.screens.destinations.SolveRecognitionTaskScreenDestination
import lab.maxb.dark.ui.theme.DarkAppTheme
import lab.maxb.dark.ui.theme.spacing
import lab.maxb.dark.ui.theme.units.sdp
import org.koin.androidx.compose.getViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Destination(
    deepLinks = [
        DeepLink(uriPattern = shareLink + FULL_ROUTE_PLACEHOLDER)
    ]
)
@Composable
fun SolveRecognitionTaskScreen(
    id: String,
    navigator: DestinationsNavigator,
    navController: NavController,
    viewModel: SolveRecognitionTaskViewModel = getViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val onEvent = viewModel::onEvent
    val snackbarState = rememberSnackbarHostState()

    LaunchedEffect(id) { viewModel.init(id) }

    ScaffoldWithDrawer(
        navController = navController,
        topBar = {
            TopBar(
                title = stringResource(id = R.string.nav_solveTask_label),
                navigationIcon = { NavBackIcon(navController = navController) },
                actions = { ShareIcon(
                    shareLink + SolveRecognitionTaskScreenDestination(id).route
                ) },
            )
        },
        snackbarState = snackbarState,
    ) {
        SolveRecognitionTaskRootStateless(uiState, onEvent)
    }

    uiState.userMessages.ChangedEffect(snackbarState, onConsumed = onEvent) {
        snackbarState show it.message
    }

    uiState.taskNotFound.ChangedEffect(onConsumed = onEvent) {
        navigator.navigateUp()
    }
}

@Preview
@Composable
fun SolveRecognitionTaskRootStatelessPreview() = SolveRecognitionTaskRootStateless(
    TaskSolveUiState(isReviewMode = true),
)

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SolveRecognitionTaskRootStateless(
    uiState: TaskSolveUiState,
    onEvent: (TaskSolveUiEvent) -> Unit = {},
) = DarkAppTheme {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageSlider(
                uiState.images,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.sdp),
            ) // TODO: On Image click open zoomable preview
            if (uiState.isReviewMode) {
                ModeratorReviewPanel(uiState.isReviewed) {
                    onEvent(TaskSolveUiEvent.MarkChanged(it))
                }
            } else {
                OutlinedTextField(
                    value = uiState.answer,
                    onValueChange = { onEvent(TaskSolveUiEvent.AnswerChanged(it)) },
                    label = { Text(stringResource(R.string.solveTask_answer)) },
                    maxLines = 1,
                    singleLine = true,
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.small),
                )
                Button(
                    modifier = Modifier.padding(MaterialTheme.spacing.normal),
                    onClick = { onEvent(TaskSolveUiEvent.SubmitTaskSolveSolution) }) {
                    Text(stringResource(id = R.string.solveTask_checkSolution))
                }
            }
        }
        LoadingScreen(show = uiState.isLoading)
    }
}

@Composable
private fun ModeratorReviewPanel(isReviewed: Boolean, onReviewChanged: (Boolean) -> Unit) {
    Row(modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small)) {
        AnimatedVisibility(visible = !isReviewed) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(MaterialTheme.spacing.small),
                colors = ButtonDefaults.buttonColors(
                    Color(0xAF736721),
                    Color.Black,
                ),
                onClick = { onReviewChanged(true) }) {
                Text(stringResource(id = R.string.solveTask_moderator_mark_reviewed))
            }
        }
        Button(
            modifier = Modifier
                .weight(1f)
                .padding(MaterialTheme.spacing.small),
            colors = ButtonDefaults.buttonColors(Color(0xAFC90A0A)),
            onClick = { onReviewChanged(false) }) {
            Text(
                stringResource(
                    if (!isReviewed) R.string.solveTask_moderator_delete
                    else R.string.solveTask_moderator_revoke_reviewed
                )
            )
        }
    }
}
