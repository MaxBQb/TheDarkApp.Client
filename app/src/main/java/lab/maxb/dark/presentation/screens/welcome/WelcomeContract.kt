package lab.maxb.dark.presentation.screens.welcome

import lab.maxb.dark.domain.model.Role
import lab.maxb.dark.domain.model.User
import lab.maxb.dark.presentation.screens.core.UiEvent
import lab.maxb.dark.presentation.screens.core.UiState

interface WelcomeUiContract {
    data class State(
        val user: User? = null,
        val role: Role? = null,
        val dailyArticle: String? = null,
    ) : UiState

    sealed interface Event : UiEvent {
        object SignOut : Event
    }
}