package lab.maxb.dark.presentation.screens.task.add

import android.app.Activity
import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import lab.maxb.dark.R
import lab.maxb.dark.presentation.components.*
import lab.maxb.dark.presentation.extra.ChangedEffect
import lab.maxb.dark.presentation.extra.ItemHolder
import lab.maxb.dark.presentation.extra.show
import lab.maxb.dark.presentation.extra.takePersistablePermission
import lab.maxb.dark.ui.theme.DarkAppTheme
import lab.maxb.dark.ui.theme.spacing
import lab.maxb.dark.ui.theme.units.sdp
import org.koin.androidx.compose.getViewModel


@OptIn(ExperimentalMaterial3Api::class)
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
                title=stringResource(id = R.string.nav_addTask_title),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.Close, null)
                    }
                },
                actions = {
                    IconButton(onClick = { onEvent(AddTaskUiEvent.Submit) }) {
                        Icon(Icons.Filled.Done, null)
                    }
                }
            )
        },
        snackbarState = snackbarState,
    ) {
        AddRecognitionTaskRootStateless(uiState, onEvent)
    }
    uiState.userMessages.ChangedEffect(snackbarState, onConsumed = onEvent) {
        snackbarState show it.message
    }
    uiState.submitSuccess.ChangedEffect(onConsumed = onEvent) {
        navController.navigateUp()
    }
}


@Preview
@Composable
fun AddRecognitionTaskRootStatelessPreview() = AddRecognitionTaskRootStateless(
    AddTaskUiState(
        names = listOf(
            ItemHolder("Some text"),
            ItemHolder(""),
        )
    )
)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AddRecognitionTaskRootStateless(
    uiState: AddTaskUiState,
    onEvent: (AddTaskUiEvent) -> Unit = {}
) = DarkAppTheme {
    Surface {
        val isVerticalOrientation = (
            LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
        )
        TwoPane(
            displayFeatures = calculateDisplayFeatures(LocalContext.current as Activity),
            strategy = if (isVerticalOrientation)
                    VerticalTwoPaneStrategy(0.5f, MaterialTheme.spacing.small)
                else HorizontalTwoPaneStrategy(0.5f),
            modifier = Modifier.fillMaxSize(),
            first = {
                InputList(
                    values = uiState.names,
                    onValueChanged = {
                        onEvent(AddTaskUiEvent.NameChanged(it))
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
                        onEvent(AddTaskUiEvent.ImagesAdded(list))
                }
                val pagerState = rememberPagerState()
                val updateImage = rememberImageRequest {
                    it?.let { image ->
                        onEvent(AddTaskUiEvent.ImageChanged(pagerState.currentPage, image))
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
                            modifier = Modifier.height(200.sdp),
                        )
                    }
                    AnimatedVisibility(
                        visible = uiState.images.isNotEmpty(),
                    ) {
                        ImageListEditPanel(
                            onAdd = getImages,
                            onEdit = updateImage,
                            onDelete = {
                                onEvent(AddTaskUiEvent.ImageRemoved(pagerState.currentPage))
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
                                .padding(MaterialTheme.spacing.normal)
                                .fillMaxSize()
                                .clickable(onClick = getImages),
                        )
                    }
                }
            }
        )
    }
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