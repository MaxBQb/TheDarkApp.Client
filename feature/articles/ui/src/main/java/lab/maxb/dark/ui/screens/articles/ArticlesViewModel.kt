package lab.maxb.dark.ui.screens.articles

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertHeaderItem
import androidx.paging.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import lab.maxb.dark.data.utils.replace
import lab.maxb.dark.domain.model.Article
import lab.maxb.dark.domain.model.Role
import lab.maxb.dark.domain.usecase.CreateArticleUseCase
import lab.maxb.dark.domain.usecase.GetArticlesListUseCase
import lab.maxb.dark.domain.usecase.UpdateArticleUseCase
import lab.maxb.dark.domain.usecase.GetProfileUseCase
import lab.maxb.dark.ui.articles.R
import lab.maxb.dark.ui.extra.FirstOnly
import lab.maxb.dark.ui.extra.anyLoading
import lab.maxb.dark.ui.extra.launch
import lab.maxb.dark.ui.extra.stateIn
import lab.maxb.dark.ui.extra.stateInAsResult
import lab.maxb.dark.ui.extra.throwIfCancellation
import lab.maxb.dark.ui.extra.uiTextOf
import lab.maxb.dark.ui.extra.valueOrNull
import lab.maxb.dark.ui.model.ArticleListItem
import lab.maxb.dark.ui.model.toPresentation
import lab.maxb.dark.ui.screens.common.Loaded
import lab.maxb.dark.ui.screens.common.Loading
import lab.maxb.dark.ui.screens.core.BaseViewModel
import org.koin.android.annotation.KoinViewModel
import lab.maxb.dark.ui.screens.articles.ArticlesUiContract as Ui

@KoinViewModel
class ArticlesViewModel(
    getProfileUseCase: GetProfileUseCase,
    getArticlesUseCase: GetArticlesListUseCase,
    val updateArticleUseCase: UpdateArticleUseCase,
    val createArticleUseCase: CreateArticleUseCase,
) : BaseViewModel<Ui.State, Ui.Event, Ui.SideEffect>() {
    private val profile = getProfileUseCase().stateInAsResult()
    private var createArticleRequest by FirstOnly()
    private var updateArticleRequest by FirstOnly()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _articles = getArticlesUseCase().mapLatest { page ->
        page.toPresentation().map { it.toPresentation() }
    }.cachedIn(viewModelScope).stateInAsResult()

    override fun getInitialState() = Ui.State()
    override val uiState =
        combine(_uiState, profile, _articles) { state, profileResult, articlesResult ->
            state.copy(
                articles = articlesResult.valueOrNull?.withExtraArticle(state)
                    ?: PagingData.empty(),
                isMutable = profileResult.valueOrNull?.role?.hasModifyArticlePermission ?: false,
                isLoading = anyLoading(profileResult, articlesResult) || state.isLoading,
            )
        }.stateIn(Ui.State())

    @OptIn(ExperimentalCoroutinesApi::class)
    val articles = uiState.mapLatest { it.articles }.cachedIn(viewModelScope)

    override fun handleEvent(event: Ui.Event): Unit = with(event) {
        when (this) {
            is Ui.Event.ArticleToggled -> toggleArticle(id)
            Ui.Event.ArticleCreationStarted -> setState {
                val article = ArticleListItem("", "", "new")
                it.copy(
                    openedArticleId = article.id,
                    openedArticle = article,
                    isEditMode = uiState.value.isMutable,
                    isCreationMode = uiState.value.isMutable,
                )
            }

            is Ui.Event.ArticleEditStarted -> setState {
                it.copy(
                    openedArticleId = article.id,
                    openedArticle = article,
                    isEditMode = uiState.value.isMutable,
                )
            }

            is Ui.Event.TitleChanged -> updateOpenedArticle {
                it.copy(title = title.take(Article.MAX_TITLE_LENGTH))
            }

            is Ui.Event.BodyChanged -> updateOpenedArticle {
                it.copy(body = body.take(Article.MAX_BODY_LENGTH))
            }

            Ui.Event.Cancel -> onCancelled()
            Ui.Event.Submit -> onSubmit()
            is Ui.Event.EffectConsumed -> handleEffectConsumption(this)
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
                setState { it.Loading }
                val (title, body, id) = article
                updateArticleUseCase(title, body, id)
                setState { it.withEditEnded() }
            } catch (e: Throwable) {
                e.throwIfCancellation()
                e.printStackTrace()
                setEffect {
                    Ui.SideEffect.UserMessage(
                        uiTextOf(R.string.articles_message_saveError)
                    )
                }
            } finally {
                setState { it.Loaded }
            }
        }
    }

    private fun createArticle(article: ArticleListItem) {
        createArticleRequest = launch {
            try {
                withLoading {
                    createArticleUseCase(article.title, article.body)
                }
                setState { it.withEditEnded() }
            } catch (e: Throwable) {
                e.throwIfCancellation()
                e.printStackTrace()
                setEffect {
                    Ui.SideEffect.UserMessage(
                        uiTextOf(R.string.articles_message_saveError)
                    )
                }
            }
        }
    }

    private fun onCancelled() = setState { it.withEditEnded() }

    private fun Ui.State.withClosedArticle() = copy(
        openedArticleId = null,
        openedArticle = null,
    )

    private fun Ui.State.withEditEnded() = copy(
        isEditMode = false,
        isCreationMode = false,
    ).withClosedArticle()

    private fun updateOpenedArticle(
        transformation: (ArticleListItem) -> ArticleListItem
    ) = setState {
        it.copy(openedArticle = transformation(it.openedArticle!!))
    }

    private fun toggleArticle(id: String) = setState { state ->
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

    private inline val Role.hasModifyArticlePermission
        get() = when (this) {
            Role.MODERATOR, Role.CONSULTOR -> true
            else -> false
        }

    private fun PagingData<ArticleListItem>.withExtraArticle(state: Ui.State) = when {
        state.isCreationMode && state.openedArticle != null -> this.insertHeaderItem(item = state.openedArticle)
        state.isEditMode -> replace(state.openedArticle) { x, y -> x.id == y.id }
        else -> this
    }
}
