package lab.maxb.dark.Domain.Model

open class Profile(
    open var login: String,
    open var user: User? = null,
    open var token: String,
    open var type: AuthType = AuthType.CREDENTIALS,
    open var role: Role = Role.USER
) {
    enum class AuthType {
        CREDENTIALS,
        OAUTH_GOOGLE
    }
}

enum class Role {
    ADMINISTRATOR,
    MODERATOR,
    CONSULTOR,
    PREMIUM_USER,
    USER,
}

fun Role.isUser() = when(this) {
    Role.USER, Role.PREMIUM_USER -> true
    else -> false
}