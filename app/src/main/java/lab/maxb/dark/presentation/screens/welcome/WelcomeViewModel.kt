package lab.maxb.dark.presentation.screens.welcome

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import lab.maxb.dark.domain.model.Role
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
import lab.maxb.dark.presentation.screens.core.BaseViewModel
import org.koin.android.annotation.KoinViewModel


@KoinViewModel
class WelcomeViewModel(
    private val signOutUseCase: SignOutUseCase,
    getCurrentUserUseCase: GetCurrentUserUseCase,
    getProfileUseCase: GetProfileUseCase,
) : BaseViewModel<Result<WelcomeUiState>, WelcomeUiEvent>, ViewModel() {
    private var signOutRequest by FirstOnly()
    private val profile = getProfileUseCase().stateInAsResult()
    private val user = getCurrentUserUseCase().stateInAsResult()

    private val _uiState = MutableStateFlow(WelcomeUiState())
    override val uiState = combine(_uiState, profile, user) { state, profileResult, userResult ->
        if (anyLoading(profileResult, userResult))
            Result.Loading
        else if (anyError(profileResult, userResult))
            Result.Error(null)
        else
            Result.Success(state.copy(
                user = userResult.valueOrNull,
                role = profileResult.valueOrNull?.role ?: Role.USER,
            ))
    }.stateIn()

    override fun onEvent(event: WelcomeUiEvent): Unit = with(event) {
        when (this) {
            is WelcomeUiEvent.SignOut -> signOut()
        }
    }

    private fun signOut() { signOutRequest = launch {
        signOutUseCase()
    } }
}
