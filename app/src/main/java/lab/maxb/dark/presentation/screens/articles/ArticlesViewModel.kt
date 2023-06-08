package lab.maxb.dark.presentation.screens.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertHeaderItem
import androidx.paging.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import lab.maxb.dark.R
import lab.maxb.dark.data.utils.pagination.replace
import lab.maxb.dark.domain.model.Article
import lab.maxb.dark.domain.model.Role
import lab.maxb.dark.domain.usecase.article.CreateArticleUseCase
import lab.maxb.dark.domain.usecase.article.GetArticlesListUseCase
import lab.maxb.dark.domain.usecase.article.UpdateArticleUseCase
import lab.maxb.dark.domain.usecase.profile.GetProfileUseCase
import lab.maxb.dark.presentation.extra.FirstOnly
import lab.maxb.dark.presentation.extra.anyLoading
import lab.maxb.dark.presentation.extra.launch
import lab.maxb.dark.presentation.extra.stateIn
import lab.maxb.dark.presentation.extra.stateInAsResult
import lab.maxb.dark.presentation.extra.throwIfCancellation
import lab.maxb.dark.presentation.extra.uiTextOf
import lab.maxb.dark.presentation.extra.valueOrNull
import lab.maxb.dark.presentation.model.ArticleListItem
import lab.maxb.dark.presentation.model.toPresentation
import lab.maxb.dark.presentation.screens.core.BaseViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ArticlesViewModel(
    getProfileUseCase: GetProfileUseCase,
    getArticlesUseCase: GetArticlesListUseCase,
    val updateArticleUseCase: UpdateArticleUseCase,
    val createArticleUseCase: CreateArticleUseCase,
) : BaseViewModel<ArticlesUiState, ArticlesUiEvent>, ViewModel() {
    private val profile = getProfileUseCase().stateInAsResult()
    private var createArticleRequest by FirstOnly()
    private var updateArticleRequest by FirstOnly()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _articles = getArticlesUseCase().mapLatest { page ->
        page.map { it.toPresentation() }
    }.cachedIn(viewModelScope).stateInAsResult()

    private val _uiState = MutableStateFlow(ArticlesUiState())
    override val uiState = combine(_uiState, profile, _articles) { state, profileResult, articlesResult ->
        state.copy(
            articles = articlesResult.valueOrNull?.withExtraArticle(state)
                ?: PagingData.empty(),
            isMutable = profileResult.valueOrNull?.role?.hasModifyArticlePermission ?: false,
            isLoading = anyLoading(profileResult, articlesResult) || state.isLoading,
        )
    }.stateIn(ArticlesUiState())

    @OptIn(ExperimentalCoroutinesApi::class)
    val articles = uiState.mapLatest { it.articles }.cachedIn(viewModelScope)

    override fun onEvent(event: ArticlesUiEvent): Unit = with(event) {
        when (this) {
            is ArticlesUiEvent.ArticleToggled -> toggleArticle(id)
            ArticlesUiEvent.ArticleCreationStarted -> _uiState.update {
                val article = ArticleListItem("", "", "new")
                it.copy(
                    openedArticleId = article.id,
                    openedArticle = article,
                    isEditMode = uiState.value.isMutable,
                    isCreationMode = uiState.value.isMutable,
                )
            }
            is ArticlesUiEvent.ArticleEditStarted -> _uiState.update { it.copy(
                openedArticleId = article.id,
                openedArticle = article,
                isEditMode = uiState.value.isMutable,
            ) }
            is ArticlesUiEvent.TitleChanged -> updateOpenedArticle {
                it.copy(title=title.take(Article.MAX_TITLE_LENGTH))
            }
            is ArticlesUiEvent.BodyChanged -> updateOpenedArticle {
                it.copy(body=body.take(Article.MAX_BODY_LENGTH))
            }
            ArticlesUiEvent.Cancel -> onCancelled()
            ArticlesUiEvent.Submit -> onSubmit()
            is ArticlesUiEvent.UserMessage -> _uiState.update {
                it.copy(userMessages = it.userMessages - this)
            }
        }
    }

    private fun onSubmit() {
        val state = uiState.value
        when {
            state.isCreationMode -> createArticle(state.openedArticle!!)
            state.isEditMode -> updateArticle(state.openedArticle!!)
        }
    }

    private fun updateArticle(article: ArticleListItem) {
        updateArticleRequest = launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                val (title, body, id) = article
                updateArticleUseCase(title, body, id)
                _uiState.update { it.withEditEnded() }
            } catch (e: Throwable) {
                e.throwIfCancellation()
                e.printStackTrace()
                _uiState.update {
                    it.copy(
                        userMessages = it.userMessages + ArticlesUiEvent.UserMessage(
                            uiTextOf(R.string.articles_message_saveError)
                        )
                    )
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun createArticle(article: ArticleListItem) {
        createArticleRequest = launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                createArticleUseCase(article.title, article.body)
                _uiState.update { it.withEditEnded() }
            } catch (e: Throwable) {
                e.throwIfCancellation()
                e.printStackTrace()
                _uiState.update {
                    it.copy(
                        userMessages = it.userMessages + ArticlesUiEvent.UserMessage(
                            uiTextOf(R.string.articles_message_saveError)
                        )
                    )
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun onCancelled() = _uiState.update { it.withEditEnded() }

    private fun ArticlesUiState.withClosedArticle() = copy(
        openedArticleId = null,
        openedArticle = null,
    )

    private fun ArticlesUiState.withEditEnded() = copy(
        isEditMode = false,
        isCreationMode = false,
    ).withClosedArticle()

    private fun updateOpenedArticle(
        transformation: (ArticleListItem) -> ArticleListItem
    ) = _uiState.update {
        it.copy(openedArticle = transformation(it.openedArticle!!))
    }

    private fun toggleArticle(id: String) = _uiState.update {state ->
        val openedId = state.openedArticleId
        state
            .let {
                if (it.isCreationMode)
                    it.withEditEnded()
                else
                    it
            }
            .let {
                if (openedId == id)
                    it.copy(openedArticleId = null)
                else
                    it.copy(openedArticleId = id)
            }
    }

    private inline val Role.hasModifyArticlePermission get() = when (this) {
        Role.MODERATOR, Role.CONSULTOR -> true
        else -> false
    }

    private fun PagingData<ArticleListItem>.withExtraArticle(state: ArticlesUiState) = when {
        state.isCreationMode && state.openedArticle != null -> this.insertHeaderItem(item=state.openedArticle)
        state.isEditMode -> replace(state.openedArticle) { x, y -> x.id == y.id }
        else -> this
    }
}
