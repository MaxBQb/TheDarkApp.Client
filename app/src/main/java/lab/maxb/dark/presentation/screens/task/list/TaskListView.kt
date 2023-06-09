package lab.maxb.dark.presentation.screens.task.list

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
import androidx.paging.compose.items
import com.bumptech.glide.load.model.GlideUrl
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import lab.maxb.dark.R
import lab.maxb.dark.presentation.components.FavoriteIcon
import lab.maxb.dark.presentation.components.LoadingCircle
import lab.maxb.dark.presentation.components.RecognitionTaskImage
import lab.maxb.dark.presentation.components.TopScaffold
import lab.maxb.dark.presentation.model.RecognitionTaskListItem
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
    navController = navController,
    title = stringResource(id = R.string.nav_taskList_title),
    content = {
        val items = viewModel.recognitionTaskList.collectAsLazyPagingItems()
        val isTaskCreationAllowed by viewModel.isTaskCreationAllowed.collectAsState()

        RecognitionTaskList(
            items,
            onItemClick = {
                navigator.navigate(SolveRecognitionTaskScreenDestination(it))
            },
            resolveImage = viewModel::getImage,
            isTaskCreationAllowed,
            viewModel::markFavorite,
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
    },
)

@Composable
fun RecognitionTaskList(
    items: LazyPagingItems<RecognitionTaskListItem>,
    onItemClick: (String) -> Unit,
    resolveImage: (String) -> GlideUrl,
    showFavorites: Boolean = false,
    onItemFavoriteToggle: (String, Boolean) -> Unit = { _, _ -> },
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(MaterialTheme.spacing.zero, MaterialTheme.spacing.small)
    ) {
        items(items = items, itemContent = { item ->
            item?.let {
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
        })
    }
}


@Composable
fun RecognitionTaskCard(
    item: RecognitionTaskListItem,
    onClick: () -> Unit,
    resolveImage: (String) -> GlideUrl,
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
