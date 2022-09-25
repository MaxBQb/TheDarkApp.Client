package lab.maxb.dark.presentation.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.lerp
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import com.google.accompanist.pager.*
import lab.maxb.dark.R
import lab.maxb.dark.presentation.extra.ChangedEffect
import lab.maxb.dark.presentation.extra.goBack
import lab.maxb.dark.presentation.extra.show
import lab.maxb.dark.presentation.viewModel.SolveRecognitionTaskViewModel
import lab.maxb.dark.presentation.viewModel.TaskSolveUiEvent
import lab.maxb.dark.presentation.viewModel.TaskSolveUiState
import lab.maxb.dark.ui.theme.DarkAppTheme
import lab.maxb.dark.ui.theme.spacing
import lab.maxb.dark.ui.theme.units.sdp
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import kotlin.math.absoluteValue

class SolveRecognitionTaskFragment : Fragment() {
    private val viewModel: SolveRecognitionTaskViewModel by sharedViewModel()
    private val args: SolveRecognitionTaskFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        viewModel.init(args.id)
        setContent { SolveRecognitionTaskRoot() }
    }

    @Composable
    private fun SolveRecognitionTaskRoot() {
        val uiState by viewModel.uiState.collectAsState()
        val onEvent = viewModel::onEvent

        SolveRecognitionTaskRootStateless(uiState, onEvent)

        uiState.userMessages.ChangedEffect(onConsumed = onEvent) { // TODO: Use scaffoldState
            it.message.show() // TODO: replace with toast (snackbar)
        }

        uiState.taskNotFound.ChangedEffect(onConsumed = onEvent) {
            goBack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.share_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    when (menuItem.itemId) {
                        R.id.menu_share -> shareTask()
                        else -> return false
                    }
                    return true
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    private fun shareTask() {
        startActivity(Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                viewModel.shareLink,
            )
            type = "text/plain"
        }, null))
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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageSlider(
    images: List<Any?>,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState(),
    onItemClick: () -> Unit = {}
) = Column(horizontalAlignment = Alignment.CenterHorizontally) {
    HorizontalPager(
        count = images.size,
        state = pagerState,
        contentPadding = PaddingValues(MaterialTheme.spacing.normal),
        modifier = modifier,
    ) { page ->
        RecognitionTaskImage(
            images[page],
            Modifier
                .fillMaxSize()
                .clickable(onClick = onItemClick)
                .graphicsLayer {
                    val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                    lerp(
                        start = 0.85f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    ).also { scale ->
                        scaleX = scale
                        scaleY = scale
                    }

                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                },
        )
    }
    AnimatedVisibility(visible = images.size > 1) {
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier.padding(MaterialTheme.spacing.extraSmall),
            activeColor = MaterialTheme.colorScheme.onBackground
        )
    }
}