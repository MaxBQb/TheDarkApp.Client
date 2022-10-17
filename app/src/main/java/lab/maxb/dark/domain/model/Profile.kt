package lab.maxb.dark.domain.model

@kotlinx.serialization.Serializable
data class Profile(
    var login: String,
    var userId: String,
    var token: String,
    var type: AuthType = AuthType.CREDENTIALS,
    var role: Role = Role.USER
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