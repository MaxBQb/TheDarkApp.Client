package lab.maxb.dark.presentation.screens.articles

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.flow.flowOf
import lab.maxb.dark.R
import lab.maxb.dark.domain.model.Article
import lab.maxb.dark.presentation.components.AnimateAppearance
import lab.maxb.dark.presentation.components.AnimatedElementSwitch
import lab.maxb.dark.presentation.components.ExpandableCard
import lab.maxb.dark.presentation.components.LoadingCircle
import lab.maxb.dark.presentation.components.LoadingScreen
import lab.maxb.dark.presentation.components.TopScaffold
import lab.maxb.dark.presentation.components.rememberSnackbarHostState
import lab.maxb.dark.presentation.components.utils.keyboardClose
import lab.maxb.dark.presentation.components.utils.keyboardNext
import lab.maxb.dark.presentation.extra.ChangedEffect
import lab.maxb.dark.presentation.extra.show
import lab.maxb.dark.presentation.model.ArticleListItem
import lab.maxb.dark.ui.theme.DarkAppTheme
import lab.maxb.dark.ui.theme.spacing
import org.koin.androidx.compose.getViewModel


@Destination
@Composable
fun ArticlesScreen(
    navController: NavController,
    viewModel: ArticlesViewModel = getViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val onEvent = viewModel::onEvent
    val items = viewModel.articles.collectAsLazyPagingItems()
    val snackbarState = rememberSnackbarHostState()

    TopScaffold(
        navController = navController,
        snackbarState = snackbarState,
        title = stringResource(id = R.string.nav_articles_title),
    ) {
        ArticlesRootStateless(items, uiState, onEvent)
    }

    uiState.userMessages.ChangedEffect(snackbarState, onConsumed = onEvent) {
        snackbarState show it.message
    }
}

@Composable
fun ArticlesRootStateless(
    items: LazyPagingItems<ArticleListItem>,
    uiState: ArticlesUiState,
    onEvent: (ArticlesUiEvent) -> Unit = {},
) = DarkAppTheme {
    Surface {
        ArticleList(
            items,
            uiState.openedArticleId,
            uiState.isMutable,
            uiState.isEditMode,
            onItemClick = { onEvent(ArticlesUiEvent.ArticleToggled(it)) },
            onItemEditClick = { onEvent(ArticlesUiEvent.ArticleEditStarted(it)) },
            onItemTitleChanged = { onEvent(ArticlesUiEvent.TitleChanged(it)) },
            onItemBodyChanged = { onEvent(ArticlesUiEvent.BodyChanged(it)) },
            onSubmit = { onEvent(ArticlesUiEvent.Submit) },
            onCancel = { onEvent(ArticlesUiEvent.Cancel) },
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            AnimatedVisibility(uiState.isMutable && !uiState.isEditMode) {
                FloatingActionButton(
                    onClick = { onEvent(ArticlesUiEvent.ArticleCreationStarted) },
                    shape = CircleShape,
                    containerColor = colorScheme.secondaryContainer,
                    modifier = Modifier
                        .padding(MaterialTheme.spacing.large)
                ) {
                    Image(painterResource(id = R.drawable.ic_plus), null)
                }
            }
        }
        LoadingScreen(show = uiState.isLoading)
    }
}

@Preview
@Composable
fun ArticlesRootStatelessPreview() = Box(Modifier.fillMaxSize()) {
    ArticlesRootStateless(
        items = listOf(
            ArticleListItem("Title here", "Long text here", ""),
            ArticleListItem("Title here 2", "Long text here 2", ""),
            ArticleListItem("Title here 3", "Long text here 3", ""),
        ).let { flowOf(PagingData.from(it)).collectAsLazyPagingItems() },
        uiState = ArticlesUiState()
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ArticleList(
    items: LazyPagingItems<ArticleListItem>,
    opened: String? = null,
    mutable: Boolean = false,
    isEditMode: Boolean = false,
    onItemClick: (String) -> Unit,
    onItemEditClick: (ArticleListItem) -> Unit,
    onItemTitleChanged: (String) -> Unit,
    onItemBodyChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    onCancel: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(MaterialTheme.spacing.zero, MaterialTheme.spacing.small)
    ) {
        items(
            count = items.itemCount,
            key = items.itemKey { it.id },
            contentType = items.itemContentType { it::class }
        ) { index ->
            val item = items[index]
            item?.let {
                AnimateAppearance(Modifier.animateItemPlacement()) {
                    ArticleCardWrapper(
                        item = it,
                        expanded = it.id == opened,
                        mutable = mutable,
                        isEditMode = isEditMode,
                        onClick = { onItemClick(it.id) },
                        onEditClick = { onItemEditClick(it) },
                        onTitleChanged = onItemTitleChanged,
                        onBodyChanged = onItemBodyChanged,
                        onSubmit = onSubmit,
                        onCancel = onCancel,
                    )
                }
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
fun ArticleCard(
    item: ArticleListItem,
    expanded: Boolean = false,
    showEditPanel: Boolean = false,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
) = ExpandableCard(
    expanded = expanded,
    title = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = item.title,
                modifier = Modifier.weight(weight = 1f, fill = false),
                overflow = TextOverflow.Ellipsis,
                maxLines = if (expanded) Int.MAX_VALUE else 1,
            )
            AnimatedVisibility(
                visible = showEditPanel && !expanded,
                modifier = Modifier.wrapContentWidth(Alignment.End),
            ) {
                Image(
                    painterResource(R.drawable.ic_edit),
                    "",
                    modifier = Modifier
                        .padding(MaterialTheme.spacing.small)
                        .clickable(onClick = onEditClick)
                        .wrapContentWidth(Alignment.End),
                )
            }
        }
    },
    body = { Text(item.body) },
    onExpandToggleClick = onClick,
)


@Composable
private fun SubmitPanel(
    onSubmit: () -> Unit,
    onCancel: () -> Unit,
) = Column(
    verticalArrangement = Arrangement.SpaceBetween,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
        .padding(top = MaterialTheme.spacing.small)
        .fillMaxHeight()
) {
    Image(
        Icons.Filled.Done,
        "",
        modifier = Modifier
            .clickable(onClick = onSubmit)
            .padding(MaterialTheme.spacing.extraSmall),
        colorFilter = ColorFilter.tint(colorScheme.onSecondary)
    )
    Image(
        Icons.Filled.Close,
        "",
        modifier = Modifier
            .clickable(onClick = onCancel)
            .padding(MaterialTheme.spacing.extraSmall),
        colorFilter = ColorFilter.tint(colorScheme.onPrimary)
    )
}

@Composable
fun EditableArticleCard(
    item: ArticleListItem,
    expanded: Boolean = false,
    onTitleChanged: (String) -> Unit,
    onBodyChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    onCancel: () -> Unit,
) = ExpandableCard(
    expanded = expanded,
    hideOnExpanded = true,
    title = {
        OutlinedTextField(
            value = item.title,
            onValueChange = onTitleChanged,
            modifier = Modifier
                .weight(1f, fill = false)
                .padding(
                    start = MaterialTheme.spacing.small,
                    top = MaterialTheme.spacing.small,
                )
                .onPreviewKeyEvent(keyboardNext.event),
            label = { Text(stringResource(R.string.articles_titleHint)) },
            keyboardOptions = keyboardNext.options,
            keyboardActions = keyboardNext.actions,
        )
        SubmitPanel(
            onSubmit = onSubmit,
            onCancel = onCancel,
        )
    },
    body = {
        OutlinedTextField(
            item.body,
            onBodyChanged,
            modifier = Modifier.onPreviewKeyEvent(keyboardClose.event),
            keyboardOptions = keyboardClose.options,
            keyboardActions = keyboardClose.actions,
            label = {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(stringResource(R.string.articles_bodyHint))
                    val showLength = item.body.length >= 100
                    if (showLength)
                        Spacer(modifier = Modifier.weight(2f))
                    AnimatedVisibility(showLength) {
                        Text(
                            stringResource(
                                id = R.string.articles_bodyLength,
                                item.body.length,
                                Article.MAX_BODY_LENGTH,
                            ),
                            color = colorScheme.onBackground
                        )
                    }
                }
            },
        )
    },
)

@Composable
fun ArticleCardWrapper(
    item: ArticleListItem,
    expanded: Boolean = false,
    mutable: Boolean = false,
    isEditMode: Boolean = false,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onTitleChanged: (String) -> Unit,
    onBodyChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    onCancel: () -> Unit,
) = AnimatedElementSwitch(
    showFirst = isEditMode && expanded,
    first = {
        EditableArticleCard(
            item = item,
            expanded = expanded,
            onTitleChanged = onTitleChanged,
            onBodyChanged = onBodyChanged,
            onSubmit = onSubmit,
            onCancel = onCancel,
        )
    },
    second = {
        ArticleCard(
            item = item,
            expanded = expanded,
            showEditPanel = mutable,
            onClick = onClick,
            onEditClick = onEditClick,
        )
    },
)


@Preview
@Composable
fun ArticleItemPreview() = DarkAppTheme {
    Surface {
        ArticleCard(
            item = ArticleListItem(
                "Title here 100000000000000000000000000000000000000000000000000000000",
                "Long text here",
                ""
            ),
            expanded = false,
            showEditPanel = true,
            onClick = {},
            onEditClick = {},
        )
    }
}

@Preview
@Composable
fun ArticleItemShortPreview() = DarkAppTheme {
    Surface {
        ArticleCard(
            item = ArticleListItem(
                "Title here",
                "Long text here",
                ""
            ),
            expanded = false,
            showEditPanel = true,
            onClick = {},
            onEditClick = {},
        )
    }
}

@Preview
@Composable
fun EditableArticleItemPreview() = DarkAppTheme {
    Surface {
        EditableArticleCard(
            item = ArticleListItem(
                "Title here",
                "Long text here",
                ""
            ),
            expanded = true,
            onTitleChanged = {},
            onBodyChanged = {},
            onSubmit = {},
            onCancel = {},
        )
    }
}
