package lab.maxb.dark.domain.model

data class AuthCredentials(
    val login: String,
    val password: String,
    val initial: Boolean = false,
)

data class ReceivedAuthCredentials(
    val token: String,
    val id: String,
    val role: Role,
    val request: AuthCredentials,
)

sealed interface AuthState {
    object Loading : AuthState
    data class Authorized(val token: String) : AuthState
    data class NotAuthorized(val previousToken: String) : AuthState
}

fun AuthState.withoutToken() = when (this) {
    is AuthState.Loading -> AuthState.NotAuthorized("")
    is AuthState.Authorized -> AuthState.NotAuthorized(token)
    is AuthState.NotAuthorized -> this
}

fun AuthState.withToken(token: String): AuthState {
    if (token.isBlank())
        return withoutToken()
    return when(this) {
        is AuthState.Loading -> AuthState.Authorized(token)
        is AuthState.Authorized -> AuthState.Authorized(token)
        is AuthState.NotAuthorized -> {
            if (previousToken != token)
                AuthState.Authorized(token)
            else
                this
        }
    }
}