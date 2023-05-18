package lab.maxb.dark.presentation.screens.articles

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.flowOf
import lab.maxb.dark.R
import lab.maxb.dark.presentation.components.ExpandableCard
import lab.maxb.dark.presentation.components.LoadingCircle
import lab.maxb.dark.presentation.components.TopScaffold
import lab.maxb.dark.presentation.components.rememberSnackbarHostState
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
        title = stringResource(id = R.string.nav_articles_title),
    ) {
        ArticlesRootStateless(items, uiState, onEvent)
    }

    uiState.userMessages.ChangedEffect(snackbarState, onConsumed = onEvent) {
        snackbarState show it.message
    }
}

@Composable
fun BoxScope.ArticlesRootStateless(
    items: LazyPagingItems<ArticleListItem>,
    uiState: ArticlesUiState,
    onEvent: (ArticlesUiEvent) -> Unit = {},
) = DarkAppTheme {
    Surface {
        ArticleList(
            items,
            uiState.openedArticleId,
            onItemClick = { onEvent(ArticlesUiEvent.ArticleToggled(it)) },
        )
        AnimatedVisibility(
            uiState.isMutable,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            FloatingActionButton(
                onClick = {},
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
}

@Preview
@Composable
fun ArticlesRootStatelessPreview() = Box(Modifier.fillMaxSize()) { ArticlesRootStateless(
    items = listOf(
        ArticleListItem("Title here", "Long text here", ""),
        ArticleListItem("Title here 2", "Long text here 2", ""),
        ArticleListItem("Title here 3", "Long text here 3", ""),
    ).let { flowOf(PagingData.from(it)).collectAsLazyPagingItems() },
    uiState = ArticlesUiState()
) }

@Composable
private fun ArticleList(
    items: LazyPagingItems<ArticleListItem>,
    opened: String? = null,
    onItemClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(MaterialTheme.spacing.zero, MaterialTheme.spacing.small)
    ) {
        items(items = items, itemContent = { item ->
            item?.let {
                ArticleCard(
                    item = it,
                    expanded = it.id == opened,
                    onClick = { onItemClick(it.id) },
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
fun ArticleCard(
    item: ArticleListItem,
    expanded: Boolean = false,
    onClick: () -> Unit,
) = ExpandableCard(
    expanded = expanded,
    title = { Text(
        text=item.title,
        overflow = TextOverflow.Ellipsis,
        maxLines = if (expanded) Int.MAX_VALUE else 1,
    ) },
    body = { Text(item.body) },
    onExpandToggleClick = onClick,
)

@Preview
@Composable
fun ArticleItemPreview() = DarkAppTheme {
    Surface {
        ArticleCard(
            item = ArticleListItem("Title here", "Long text here", ""),
            expanded = true,
            onClick = {},
        )
    }
}

