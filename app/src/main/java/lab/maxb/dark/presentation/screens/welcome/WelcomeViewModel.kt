package lab.maxb.dark.presentation.screens.welcome

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import lab.maxb.dark.domain.model.isUser
import lab.maxb.dark.domain.usecase.auth.SignOutUseCase
import lab.maxb.dark.domain.usecase.profile.GetProfileUseCase
import lab.maxb.dark.domain.usecase.user.GetCurrentUserUseCase
import lab.maxb.dark.presentation.extra.*
import javax.inject.Inject


@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    getCurrentUserUseCase: GetCurrentUserUseCase,
    getProfileUseCase: GetProfileUseCase,
) : ViewModel() {
    private var signOutRequest by FirstOnly()
    private val profile = getProfileUseCase().stateInAsResult()
    private val user = getCurrentUserUseCase().stateInAsResult()

    private val _uiState = MutableStateFlow(WelcomeUiState())
    val uiState = combine(_uiState, profile, user) { state, profileResult, userResult ->
        if (anyLoading(profileResult, userResult))
            Result.Loading
        else if (anyError(profileResult, userResult))
            Result.Error(null)
        else
            Result.Success(state.copy(
                user = userResult.valueOrNull,
                isUser = profileResult.valueOrNull?.role?.isUser == true,
            ))
    }.stateIn()

    fun onEvent(event: WelcomeUiEvent) = with(event) {
        when (this) {
            is WelcomeUiEvent.SignOut -> signOut()
        }
    }

    private fun signOut() { signOutRequest = launch {
        signOutUseCase()
    } }
}
