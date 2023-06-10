package lab.maxb.dark.presentation.screens.task.solve

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.FULL_ROUTE_PLACEHOLDER
import lab.maxb.dark.R
import lab.maxb.dark.domain.operations.shareLink
import lab.maxb.dark.presentation.components.FavoriteIcon
import lab.maxb.dark.presentation.components.ImageSlider
import lab.maxb.dark.presentation.components.LoadingScreen
import lab.maxb.dark.presentation.components.NavBackIcon
import lab.maxb.dark.presentation.components.ScaffoldWithDrawer
import lab.maxb.dark.presentation.components.ShareIcon
import lab.maxb.dark.presentation.components.TopBar
import lab.maxb.dark.presentation.components.ZoomIcon
import lab.maxb.dark.presentation.components.rememberSnackbarHostState
import lab.maxb.dark.presentation.extra.ChangedEffect
import lab.maxb.dark.presentation.extra.show
import lab.maxb.dark.presentation.screens.destinations.SolveRecognitionTaskScreenDestination
import lab.maxb.dark.ui.theme.DarkAppTheme
import lab.maxb.dark.ui.theme.spacing
import lab.maxb.dark.ui.theme.units.sdp
import org.koin.androidx.compose.getViewModel


@Destination(
    deepLinks = [
        DeepLink(uriPattern = shareLink + FULL_ROUTE_PLACEHOLDER)
    ]
)
@Composable
fun SolveRecognitionTaskScreen(
    id: String,
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
                title = stringResource(
                    if (uiState.isReviewMode)
                        R.string.nav_taskReview_title
                    else
                        R.string.nav_solveTask_title
                ),
                navigationIcon = { NavBackIcon(navController = navController) },
                actions = {
                    ZoomIcon(value = uiState.zoomEnabled) {
                        onEvent(TaskSolveUiEvent.ZoomToggled(uiState.zoomEnabled))
                    }
                    AnimatedVisibility(!uiState.isReviewMode && uiState.isFavorite != null) {
                        FavoriteIcon(uiState.isFavorite ?: false) {
                            onEvent(TaskSolveUiEvent.MarkFavorite(id, it))
                        }
                    }
                    ShareIcon(shareLink + SolveRecognitionTaskScreenDestination(id).route)
                },
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
        navController.navigateUp()
    }
}

@Preview
@Composable
fun SolveRecognitionTaskRootStatelessPreview() = SolveRecognitionTaskRootStateless(
    TaskSolveUiState(isReviewMode = true),
)


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
                    .height(300.sdp),
                zoomable = uiState.zoomEnabled,
            )
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ModeratorReviewPanel(isReviewed: Boolean, onReviewChanged: (Boolean) -> Unit) {
    AnimatedContent(targetState = isReviewed) { reviewedState ->
        Row(modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small)) {
            if (!reviewedState) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .padding(MaterialTheme.spacing.small)
                        .animateEnterExit(enter = fadeIn()),
                    colors = ButtonDefaults.buttonColors(
                        Color(0xAF736721),
                        Color.Black,
                    ),
                    onClick = { onReviewChanged(true) }
                ) {
                    Text(stringResource(R.string.solveTask_moderator_mark_reviewed))
                }
                Spacer(modifier = Modifier
                    .weight(0.25f)
                    .animateEnterExit())
            }

            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(MaterialTheme.spacing.small)
                    .animateContentSize(),
                colors = ButtonDefaults.buttonColors(Color(0xAFC90A0A)),
                onClick = { onReviewChanged(false) }) {
                Text(
                    stringResource(
                        if (!reviewedState) R.string.solveTask_moderator_delete
                        else R.string.solveTask_moderator_revoke_reviewed
                    )
                )
            }
        }
    }
}
