package lab.maxb.dark.ui.screens.welcome

import kotlinx.coroutines.flow.combine
import lab.maxb.dark.domain.usecase.GetDailyArticleUseCase
import lab.maxb.dark.domain.usecase.SignOutUseCase
import lab.maxb.dark.domain.usecase.GetProfileUseCase
import lab.maxb.dark.domain.usecase.GetCurrentUserUseCase
import lab.maxb.dark.ui.extra.FirstOnly
import lab.maxb.dark.ui.extra.Result
import lab.maxb.dark.ui.extra.anyError
import lab.maxb.dark.ui.extra.anyLoading
import lab.maxb.dark.ui.extra.launch
import lab.maxb.dark.ui.extra.stateIn
import lab.maxb.dark.ui.extra.stateInAsResult
import lab.maxb.dark.ui.extra.valueOrNull
import lab.maxb.dark.ui.screens.core.PureInteractiveViewModel
import org.koin.android.annotation.KoinViewModel
import lab.maxb.dark.ui.screens.welcome.WelcomeUiContract as Ui


@KoinViewModel
class WelcomeViewModel(
    private val signOutUseCase: SignOutUseCase,
    getCurrentUserUseCase: GetCurrentUserUseCase,
    getProfileUseCase: GetProfileUseCase,
    getDailyArticleUseCase: GetDailyArticleUseCase,
) : PureInteractiveViewModel<Result<Ui.State>, Ui.Event>() {
    private var signOutRequest by FirstOnly()
    private val profile = getProfileUseCase().stateInAsResult()
    private val user = getCurrentUserUseCase().stateInAsResult()
    private val dailyArticle = getDailyArticleUseCase().stateInAsResult()

    override fun getInitialState() = Result.Success(Ui.State())
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

    override fun handleEvent(event: Ui.Event): Unit = with(event) {
        when (this) {
            is Ui.Event.SignOut -> signOut()
        }
    }

    private fun signOut() { signOutRequest = launch {
        signOutUseCase()
    } }
}
