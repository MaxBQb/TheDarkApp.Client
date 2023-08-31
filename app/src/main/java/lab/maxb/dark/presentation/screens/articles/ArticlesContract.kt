package lab.maxb.dark.presentation.screens.articles

import androidx.paging.PagingData
import lab.maxb.dark.presentation.extra.UiText
import lab.maxb.dark.presentation.model.ArticleListItem
import lab.maxb.dark.presentation.screens.core.UiEvent
import lab.maxb.dark.presentation.screens.core.effects.EffectKey
import lab.maxb.dark.presentation.screens.core.effects.EmptyEffectsHolder
import lab.maxb.dark.presentation.screens.core.effects.UiEffectAwareState
import lab.maxb.dark.presentation.screens.core.effects.UiSideEffect
import lab.maxb.dark.presentation.screens.core.effects.UiSideEffectConsumed
import lab.maxb.dark.presentation.screens.core.effects.UiSideEffectsHolder

data class ArticlesUiState(
    val articles: PagingData<ArticleListItem> = PagingData.empty(),
    val openedArticleId: String? = null,
    val openedArticle: ArticleListItem? = null,
    val isEditMode: Boolean = false,
    val isCreationMode: Boolean = false,
    val isMutable: Boolean = false,
    val isLoading: Boolean = false,
    override val sideEffectsHolder: UiSideEffectsHolder = EmptyEffectsHolder,
) : UiEffectAwareState {
    override fun clone(sideEffectsHolder: UiSideEffectsHolder)
        = copy(sideEffectsHolder=sideEffectsHolder)
}

sealed interface ArticlesUiEvent : UiEvent {
    data class ArticleToggled(val id: String) : ArticlesUiEvent
    data class ArticleEditStarted(val article: ArticleListItem) : ArticlesUiEvent
    object ArticleCreationStarted : ArticlesUiEvent
    data class TitleChanged(val title: String) : ArticlesUiEvent
    data class BodyChanged(val body: String) : ArticlesUiEvent
    object Submit : ArticlesUiEvent
    object Cancel : ArticlesUiEvent
    data class EffectConsumed(override val effect: EffectKey) : UiSideEffectConsumed, ArticlesUiEvent
}

sealed interface ArticlesUiSideEffect: UiSideEffect {
    data class UserMessage(val message: UiText) : ArticlesUiSideEffect
}