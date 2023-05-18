package lab.maxb.dark.presentation.screens.welcome

import lab.maxb.dark.domain.model.Role
import lab.maxb.dark.domain.model.User
import lab.maxb.dark.presentation.screens.core.UiEvent
import lab.maxb.dark.presentation.screens.core.UiState

data class WelcomeUiState(
    val user: User? = null,
    val role: Role = Role.USER,
) : UiState

sealed interface WelcomeUiEvent : UiEvent {
    object SignOut : WelcomeUiEvent
}