package lab.maxb.dark.presentation.screens.articles

import androidx.paging.PagingData
import lab.maxb.dark.presentation.extra.UiText
import lab.maxb.dark.presentation.extra.UiTrigger
import lab.maxb.dark.presentation.extra.UiTriggers
import lab.maxb.dark.presentation.model.ArticleListItem
import lab.maxb.dark.presentation.screens.core.UiEvent
import lab.maxb.dark.presentation.screens.core.UiState

data class ArticlesUiState(
    val articles: PagingData<ArticleListItem> = PagingData.empty(),
    val openedArticleId: String? = null,
    val openedArticle: ArticleListItem? = null,
    val isEditMode: Boolean = false,
    val isCreationMode: Boolean = false,
    val userMessages: UiTriggers<ArticlesUiEvent.UserMessage> = UiTriggers(),
    val isMutable: Boolean = false,
    val isLoading: Boolean = false,
) : UiState

sealed interface ArticlesUiEvent : UiEvent {
    data class ArticleToggled(val id: String) : ArticlesUiEvent
    data class ArticleEditStarted(val article: ArticleListItem) : ArticlesUiEvent
    object ArticleCreationStarted : ArticlesUiEvent
    data class TitleChanged(val title: String) : ArticlesUiEvent
    data class BodyChanged(val body: String) : ArticlesUiEvent
    object Submit : ArticlesUiEvent
    object Cancel : ArticlesUiEvent

    // UiTriggers
    data class UserMessage(val message: UiText) : UiTrigger(), ArticlesUiEvent
}