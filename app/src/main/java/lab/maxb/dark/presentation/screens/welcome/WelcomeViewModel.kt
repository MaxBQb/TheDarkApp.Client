package lab.maxb.dark.presentation.screens.welcome

import kotlinx.coroutines.flow.combine
import lab.maxb.dark.domain.usecase.article.GetDailyArticleUseCase
import lab.maxb.dark.domain.usecase.auth.SignOutUseCase
import lab.maxb.dark.domain.usecase.profile.GetProfileUseCase
import lab.maxb.dark.domain.usecase.user.GetCurrentUserUseCase
import lab.maxb.dark.presentation.extra.FirstOnly
import lab.maxb.dark.presentation.extra.Result
import lab.maxb.dark.presentation.extra.anyError
import lab.maxb.dark.presentation.extra.anyLoading
import lab.maxb.dark.presentation.extra.launch
import lab.maxb.dark.presentation.extra.stateIn
import lab.maxb.dark.presentation.extra.stateInAsResult
import lab.maxb.dark.presentation.extra.valueOrNull
import lab.maxb.dark.presentation.screens.core.PureInteractiveViewModel
import org.koin.android.annotation.KoinViewModel


@KoinViewModel
class WelcomeViewModel(
    private val signOutUseCase: SignOutUseCase,
    getCurrentUserUseCase: GetCurrentUserUseCase,
    getProfileUseCase: GetProfileUseCase,
    getDailyArticleUseCase: GetDailyArticleUseCase,
) : PureInteractiveViewModel<Result<WelcomeUiState>, WelcomeUiEvent>() {
    private var signOutRequest by FirstOnly()
    private val profile = getProfileUseCase().stateInAsResult()
    private val user = getCurrentUserUseCase().stateInAsResult()
    private val dailyArticle = getDailyArticleUseCase().stateInAsResult()

    override fun getInitialState() = Result.Success(WelcomeUiState())
    override val uiState = combine(_uiState, profile, user, dailyArticle)
    { state, profileResult, userResult, dailyArticleResult ->
        if (anyLoading(profileResult, userResult))
            Result.Loading
        else if (anyError(profileResult, userResult))
            Result.Error(null)
        else
            Result.Success(state.valueOrNull!!.copy(
                user = userResult.valueOrNull,
                role = profileResult.valueOrNull?.role,
                dailyArticle = dailyArticleResult.valueOrNull?.body,
            ))
    }.stateIn()

    override fun handleEvent(event: WelcomeUiEvent): Unit = with(event) {
        when (this) {
            is WelcomeUiEvent.SignOut -> signOut()
        }
    }

    private fun signOut() { signOutRequest = launch {
        signOutUseCase()
    } }
}
