package lab.maxb.dark.ui.screens.tasks_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.ramcosta.composedestinations.annotation.Destination
import lab.maxb.dark.ui.components.FavoriteIcon
import lab.maxb.dark.ui.components.LoadingCircle
import lab.maxb.dark.ui.components.RecognitionTaskImage
import lab.maxb.dark.ui.components.TopScaffold
import lab.maxb.dark.ui.model.DrawerDestination
import lab.maxb.dark.ui.model.RecognitionTaskListItem
import lab.maxb.dark.ui.theme.spacing
import lab.maxb.dark.ui.theme.units.sdp
import org.koin.androidx.compose.getViewModel
import lab.maxb.dark.ui.core.R as coreR

interface TaskListNavigator {
    fun navigateSolveTask(id: String)
    fun navigateAddTask()
}

@Destination
@Composable
fun RecognitionTaskListScreen(
    navigator: TaskListNavigator,
    navController: NavController,
    viewModel: RecognitionTaskListViewModel = getViewModel(),
) = TopScaffold(
    navController = navController,
    title = stringResource(DrawerDestination.Tasks.label),
    content = {
        val items = viewModel.recognitionTaskList.collectAsLazyPagingItems()
        val isTaskCreationAllowed by viewModel.isTaskCreationAllowed.collectAsState()

        RecognitionTaskList(
            items,
            onItemClick = { navigator.navigateSolveTask(it) },
            resolveImage = viewModel::getImage,
            isTaskCreationAllowed,
            viewModel::markFavorite,
        )
        AnimatedVisibility(
            isTaskCreationAllowed,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            FloatingActionButton(
                onClick = { navigator.navigateAddTask() },
                shape = CircleShape,
                containerColor = colorScheme.secondaryContainer,
                modifier = Modifier
                    .padding(MaterialTheme.spacing.large)
                    .align(Alignment.BottomEnd)
            ) {
                Image(painterResource(coreR.drawable.ic_plus), null)
            }
        }
    },
)

@Composable
fun RecognitionTaskList(
    items: LazyPagingItems<RecognitionTaskListItem>,
    onItemClick: (String) -> Unit,
    resolveImage: (String) -> String,
    showFavorites: Boolean = false,
    onItemFavoriteToggle: (String, Boolean) -> Unit = { _, _ -> },
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(MaterialTheme.spacing.zero, MaterialTheme.spacing.small)
    ) {
        items(
            count = items.itemCount,
            key = items.itemKey(),
            contentType = items.itemContentType()
        ) { i ->
            items[i]?.let {
                RecognitionTaskCard(
                    it,
                    onClick = { onItemClick(it.id) },
                    resolveImage = resolveImage,
                    showFavorites = showFavorites,
                    onFavoriteToggled = { x -> onItemFavoriteToggle(it.id, x) }
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
        }
    }
}


@Composable
fun RecognitionTaskCard(
    item: RecognitionTaskListItem,
    onClick: () -> Unit,
    resolveImage: (String) -> String,
    showFavorites: Boolean = false,
    onFavoriteToggled: (Boolean) -> Unit = {},
) {
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
                resolveImage(item.image),
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
                Text(item.ownerName)
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.sdp),
                contentAlignment = Alignment.TopStart
            ) {
                AnimatedVisibility(showFavorites) {
                    FavoriteIcon(
                        item.favorite,
                        Modifier.scale(1.15f),
                        onFavoriteToggled,
                    )
                }
            }
        }
    }
}
