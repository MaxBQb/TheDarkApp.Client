package lab.maxb.dark.ui.screens.task.add

import android.app.Activity
import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import com.google.accompanist.adaptive.VerticalTwoPaneStrategy
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.ramcosta.composedestinations.annotation.Destination
import lab.maxb.dark.ui.R
import lab.maxb.dark.ui.components.ImageListEditPanel
import lab.maxb.dark.ui.components.ImageSlider
import lab.maxb.dark.ui.components.InputList
import lab.maxb.dark.ui.components.LoadingScreen
import lab.maxb.dark.ui.components.ScaffoldWithDrawer
import lab.maxb.dark.ui.components.TopBar
import lab.maxb.dark.ui.components.rememberSnackbarHostState
import lab.maxb.dark.ui.extra.ItemHolder
import lab.maxb.dark.ui.extra.show
import lab.maxb.dark.data.utils.takePersistablePermission
import lab.maxb.dark.ui.screens.core.effects.EffectKey
import lab.maxb.dark.ui.screens.core.effects.On
import lab.maxb.dark.ui.screens.core.effects.SideEffects
import lab.maxb.dark.ui.screens.core.effects.UiSideEffectsHolder
import lab.maxb.dark.ui.theme.spacing
import lab.maxb.dark.ui.theme.units.sdp
import org.koin.androidx.compose.getViewModel
import lab.maxb.dark.ui.screens.task.add.AddTaskUiContract as Ui


@Destination
@Composable
fun AddRecognitionTaskScreen(
    navController: NavController,
    viewModel: AddRecognitionTaskViewModel = getViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val onEvent = viewModel::onEvent
    val snackbarState = rememberSnackbarHostState()

    ScaffoldWithDrawer(
        navController = navController,
        topBar = {
            TopBar(
                title = stringResource(id = R.string.nav_addTask_title),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.Close, null)
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onEvent(Ui.Event.Submit) },
                        enabled = !uiState.isLoading,
                    ) {
                        Icon(Icons.Filled.Done, null)
                    }
                }
            )
        },
        snackbarState = snackbarState,
    ) {
        AddRecognitionTaskRootStateless(uiState, onEvent)
    }

    ApplySideEffects(
        uiState.sideEffectsHolder,
        { onEvent(Ui.Event.EffectConsumed(it)) },
        snackbarState,
        navController,
    )
}

@Composable
private fun ApplySideEffects(
    effects: UiSideEffectsHolder,
    onConsumed: (EffectKey) -> Unit,
    snackbarState: SnackbarHostState,
    navController: NavController,
) {
    SideEffects(effects, onConsumed) {
        On<Ui.SideEffect.UserMessage>(false, snackbarState) {
            it.message.show(snackbarState)
        }
        On<Ui.SideEffect.SubmitSuccess>(true) {
            navController.navigateUp()
        }
    }
}


@Preview
@Composable
fun AddRecognitionTaskRootStatelessPreview() = AddRecognitionTaskRootStateless(
    Ui.State(
        names = listOf(
            ItemHolder("Some text"),
            ItemHolder(""),
        )
    )
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddRecognitionTaskRootStateless(
    uiState: Ui.State,
    onEvent: (Ui.Event) -> Unit = {}
) {
    val isVerticalOrientation = LocalConfiguration.current.orientation ==
            Configuration.ORIENTATION_PORTRAIT
    TwoPane(
        displayFeatures = calculateDisplayFeatures(LocalContext.current as Activity),
        strategy = if (isVerticalOrientation)
            VerticalTwoPaneStrategy(0.466f, MaterialTheme.spacing.small)
        else HorizontalTwoPaneStrategy(0.5f),
        modifier = Modifier.fillMaxSize(),
        first = {
            InputList(
                values = uiState.names,
                onValueChanged = {
                    onEvent(Ui.Event.NameChanged(it))
                },
                suggestions = uiState.suggestions,
                queryLabel = {
                    Text(
                        stringResource(R.string.addTask_inputList_hint),
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = MaterialTheme.spacing.extraSmall)
            )
        },
        second = {
            val context = LocalContext.current.applicationContext
            val getImages = rememberImagesRequest { result ->
                val list = result?.filterNotNull()?.map {
                    it.takePersistablePermission(context)
                    it
                }
                if (!list.isNullOrEmpty())
                    onEvent(Ui.Event.ImagesAdded(list))
            }
            val pagerState = rememberPagerState(
                initialPage = 0,
                initialPageOffsetFraction = 0f
            ) { uiState.images.size }
            val updateImage = rememberImageRequest {
                it?.let { image ->
                    onEvent(Ui.Event.ImageChanged(pagerState.currentPage, image))
                }
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                AnimatedVisibility(
                    visible = uiState.images.isEmpty(),
                ) {
                    Text(
                        text = stringResource(R.string.addTask_loadImagesTitle),
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(MaterialTheme.spacing.extraSmall)
                    )
                }
                AnimatedVisibility(
                    visible = uiState.images.isNotEmpty(),
                ) {
                    ImageSlider(
                        images = uiState.images,
                        pagerState = pagerState,
                        modifier = Modifier.height(175.sdp),
                    )
                }
                AnimatedVisibility(
                    visible = uiState.images.isNotEmpty(),
                ) {
                    ImageListEditPanel(
                        onAdd = getImages,
                        onEdit = updateImage,
                        onDelete = {
                            onEvent(Ui.Event.ImageRemoved(pagerState.currentPage))
                        },
                        allowAddition = uiState.allowedImageCount > 0
                    )
                }
                AnimatedVisibility(
                    visible = uiState.images.isEmpty(),
                ) {
                    Image(
                        painterResource(R.drawable.ic_add_photo_alternative),
                        "",
                        modifier = Modifier
                            .padding(bottom = MaterialTheme.spacing.normal)
                            .fillMaxSize()
                            .clickable(onClick = getImages),
                    )
                }
            }
        }
    )
    LoadingScreen(show = uiState.isLoading)
}


@Composable
fun rememberImagesRequest(onResult: (List<Uri?>?) -> Unit): () -> Unit {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenMultipleDocuments(),
        onResult
    )
    return { launcher.launch(arrayOf("image/*")) }
}

@Composable
fun rememberImageRequest(onResult: (Uri?) -> Unit): () -> Unit {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument(),
        onResult
    )
    return { launcher.launch(arrayOf("image/*")) }
}
