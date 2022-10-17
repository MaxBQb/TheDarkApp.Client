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