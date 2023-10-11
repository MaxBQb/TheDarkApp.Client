package lab.maxb.dark.domain.model

data class Profile(
    val login: String,
    val userId: String,
    val token: String,
    val type: AuthType = AuthType.CREDENTIALS,
    val role: Role = Role.USER
) {
    enum class AuthType {
        CREDENTIALS,
    }
}

enum class Role {
    ADMINISTRATOR,
    MODERATOR,
    CONSULTOR,
    PREMIUM_USER,
    USER,
}

val Role.isUser get() = when(this) {
    Role.USER, Role.PREMIUM_USER -> true
    else -> false
}
