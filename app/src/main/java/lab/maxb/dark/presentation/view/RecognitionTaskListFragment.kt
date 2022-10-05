package lab.maxb.dark.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.bumptech.glide.load.model.GlideUrl
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.animation.crossfade.CrossfadePlugin
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import lab.maxb.dark.R
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.presentation.view.destinations.AddRecognitionTaskScreenDestination
import lab.maxb.dark.presentation.view.destinations.SolveRecognitionTaskScreenDestination
import lab.maxb.dark.presentation.viewModel.RecognitionTaskListViewModel
import lab.maxb.dark.ui.theme.spacing
import lab.maxb.dark.ui.theme.units.sdp
import org.koin.androidx.compose.getViewModel


@Destination
@Composable
fun RecognitionTaskListScreen(
    navigator: DestinationsNavigator,
    navController: NavController,
    viewModel: RecognitionTaskListViewModel = getViewModel(),
) = TopScaffold(
    title = stringResource(id = R.string.nav_taskList_label),
    navController = navController,
) {
    val items = viewModel.recognitionTaskList.collectAsLazyPagingItems()
    val isTaskCreationAllowed by viewModel.isTaskCreationAllowed.collectAsState()

    RecognitionTaskList(
        items,
        onItemClick = {
            navigator.navigate(SolveRecognitionTaskScreenDestination(it.id))
        },
        resolveImage = viewModel::getImage
    )
    if (isTaskCreationAllowed)
        FloatingActionButton(
            onClick = {
                navigator.navigate(AddRecognitionTaskScreenDestination())
            },
            shape = CircleShape,
            containerColor = colorScheme.secondaryContainer,
            modifier = Modifier
                .padding(MaterialTheme.spacing.large)
                .align(Alignment.BottomEnd)
        ) {
            Image(painterResource(id = R.drawable.ic_plus), null)
        }
}

@Composable
private fun RecognitionTaskList(
    items: LazyPagingItems<RecognitionTask>,
    onItemClick: (RecognitionTask) -> Unit,
    resolveImage: (String) -> GlideUrl
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(MaterialTheme.spacing.zero, MaterialTheme.spacing.small)
    ) {
        items(items = items, itemContent = { item ->
            item?.let {
                RecognitionTaskCard(
                    it,
                    onClick = { onItemClick(it) },
                    resolveImage = resolveImage
                )
            } ?: run {
                LoadingCircle(
                    Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.normal, MaterialTheme.spacing.small)
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    5
                )
            }
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecognitionTaskCard(
    item: RecognitionTask,
    onClick: () -> Unit,
    resolveImage: (String) -> GlideUrl
) {
    val taskOwnerName = item.owner?.name ?: ""
    val elevation = if (item.reviewed) 300 else 500
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.sdp)
            .padding(MaterialTheme.spacing.normal, MaterialTheme.spacing.small),
        tonalElevation = elevation.dp,
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            RecognitionTaskImage(
                resolveImage(item.images?.firstOrNull() ?: ""),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.sdp),
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                colorScheme.surfaceColorAtElevation(elevation.dp),
                                Color.Transparent
                            ),
                            endY = 100f
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.sdp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(taskOwnerName)
            }
        }
    }
}

@Composable
fun RecognitionTaskImage(
    imageModel: Any?,
    modifier: Modifier = Modifier,
    imageOptions: ImageOptions = ImageOptions(contentScale = ContentScale.Inside)
) = GlideImage(
    imageModel,
    modifier = modifier,
    imageOptions = imageOptions,
    failure = {
        LoadingError(
            Modifier
                .fillMaxSize()
                .padding(16.sdp)
        )
    },
    loading = {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingCircle(0.7f)
        }
    },
    component = rememberImageComponent {
        +CrossfadePlugin(duration = 1500)
    }
)

@Composable
fun LoadingError(modifier: Modifier = Modifier) {
    Image(
        painterResource(R.drawable.ic_error),
        null,
        modifier
    )
}

@Composable
fun LoadingCircle(modifier: Modifier = Modifier, width: Int = 5) {
    val color = colorScheme.onSurface.copy(0.1f)
    val strokeWidth = width.sdp
    if (LocalView.current.isInEditMode)
        CircularProgressIndicator(
            0.45f,
            modifier,
            color,
            strokeWidth,
        )
    else
        CircularProgressIndicator(
            modifier,
            color,
            strokeWidth,
        )
}

@Composable
fun LoadingCircle(size: Float, modifier: Modifier = Modifier, width: Int = 5)
    = LoadingCircle(modifier.fillMaxSize(size/2f), width)