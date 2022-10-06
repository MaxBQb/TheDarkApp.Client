package lab.maxb.dark.presentation.screens.welcome

import lab.maxb.dark.domain.model.User

data class WelcomeUiState(
    val user: User? = null,
    val isUser: Boolean = false,
)

sealed interface WelcomeUiEvent {
    object SignOut : WelcomeUiEvent
}