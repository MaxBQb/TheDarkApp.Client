package lab.maxb.dark.presentation.screens.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import lab.maxb.dark.domain.model.Role
import lab.maxb.dark.domain.usecase.article.GetArticlesListUseCase
import lab.maxb.dark.domain.usecase.profile.GetProfileUseCase
import lab.maxb.dark.presentation.extra.anyLoading
import lab.maxb.dark.presentation.extra.stateIn
import lab.maxb.dark.presentation.extra.stateInAsResult
import lab.maxb.dark.presentation.extra.valueOrNull
import lab.maxb.dark.presentation.model.toPresentation
import lab.maxb.dark.presentation.screens.core.BaseViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ArticlesViewModel(
    getProfileUseCase: GetProfileUseCase,
    getArticlesUseCase: GetArticlesListUseCase,
) : BaseViewModel<ArticlesUiState, ArticlesUiEvent>, ViewModel() {
    private val profile = getProfileUseCase().stateInAsResult()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _articles = getArticlesUseCase().mapLatest { page ->
        page.map { it.toPresentation() }
    }.cachedIn(viewModelScope).stateInAsResult()

    private val _uiState = MutableStateFlow(ArticlesUiState())
    override val uiState = combine(_uiState, profile, _articles) { state, profileResult, articlesResult ->
        state.copy(
            articles = articlesResult.valueOrNull ?: PagingData.empty(),
            isMutable = profileResult.valueOrNull?.role?.hasModifyArticlePermission ?: false,
            isLoading = anyLoading(profileResult, articlesResult) || state.isLoading
        )
    }.stateIn(ArticlesUiState())

    @OptIn(ExperimentalCoroutinesApi::class)
    val articles = uiState.mapLatest { it.articles }.cachedIn(viewModelScope)

    override fun onEvent(event: ArticlesUiEvent): Unit = with(event) {
        when (this) {
            is ArticlesUiEvent.ArticleToggled -> toggleArticle(id)
            else -> TODO()
        }
    }

    private fun toggleArticle(id: String) = _uiState.update {
        if (it.openedArticleId == id)
            it.copy(openedArticleId = null)
        else
            it.copy(openedArticleId = id)
    }

    private inline val Role.hasModifyArticlePermission get() = when (this) {
        Role.MODERATOR, Role.CONSULTOR -> true
        else -> false
    }
}