package lab.maxb.dark.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.bumptech.glide.load.model.GlideUrl
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.animation.crossfade.CrossfadePlugin
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import lab.maxb.dark.R
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.presentation.extra.navigate
import lab.maxb.dark.presentation.viewModel.RecognitionTaskListViewModel
import lab.maxb.dark.ui.theme.DarkAppTheme
import lab.maxb.dark.ui.theme.spacing
import lab.maxb.dark.ui.theme.units.sdp
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class RecognitionTaskListFragment : Fragment() {
    private val mViewModel: RecognitionTaskListViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            RecognitionTaskListRoot(
                mViewModel,
                onItemClick = {
                    RecognitionTaskListFragmentDirections
                        .navToSolveTaskFragment(it.id).navigate()
                },
                onFabClick = {
                    RecognitionTaskListFragmentDirections
                        .navToAddTaskFragment().navigate()
                }
            )
        }
    }
}

@Composable
fun RecognitionTaskListRoot(
    viewModel: RecognitionTaskListViewModel,
    onItemClick: (RecognitionTask) -> Unit,
    onFabClick: () -> Unit,
) = DarkAppTheme {
    val items = viewModel.recognitionTaskList.collectAsLazyPagingItems()
    val isTaskCreationAllowed by viewModel.isTaskCreationAllowed.collectAsState()

    Surface {
        RecognitionTaskList(
            items,
            onItemClick = onItemClick,
            resolveImage = viewModel::getImage
        )
        if (isTaskCreationAllowed)
            FloatingActionButton(
                onClick = onFabClick,
                shape = CircleShape,
                containerColor = colorScheme.secondaryContainer,
                modifier = Modifier.padding(MaterialTheme.spacing.large)
                    .wrapContentSize(Alignment.BottomEnd)
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
            GlideImage(
                resolveImage(item.images?.firstOrNull() ?: ""),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.sdp),
                imageOptions = ImageOptions(contentScale = ContentScale.Inside),
                failure = {
                    LoadingError(
                        Modifier
                            .fillMaxSize()
                            .padding(16.sdp))
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
fun LoadingError(modifier: Modifier = Modifier) {
    Image(
        painterResource(R.drawable.ic_error),
        null,
        modifier
    )
}

@Composable
fun LoadingCircle(modifier: Modifier = Modifier, width: Int = 5) {
    CircularProgressIndicator(
        modifier,
        color = colorScheme.onSurface.copy(0.1f),
        strokeWidth = width.sdp
    )
}

@Composable
fun LoadingCircle(size: Float, modifier: Modifier = Modifier, width: Int = 5)
    = LoadingCircle(modifier.fillMaxSize(size/2f), width)