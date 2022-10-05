package lab.maxb.dark.presentation.screens.task.list

import androidx.compose.animation.AnimatedVisibility
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
import lab.maxb.dark.R
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.presentation.components.LoadingCircle
import lab.maxb.dark.presentation.components.RecognitionTaskImage
import lab.maxb.dark.presentation.components.TopScaffold
import lab.maxb.dark.presentation.screens.destinations.AddRecognitionTaskScreenDestination
import lab.maxb.dark.presentation.screens.destinations.SolveRecognitionTaskScreenDestination
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
    AnimatedVisibility(
        isTaskCreationAllowed,
        modifier = Modifier.align(Alignment.BottomEnd)
    ) {
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
