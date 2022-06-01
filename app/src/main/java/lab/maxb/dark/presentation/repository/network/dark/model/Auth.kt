package lab.maxb.dark.presentation.repository.network.dark.model

import lab.maxb.dark.domain.model.Profile
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.domain.model.Role
import lab.maxb.dark.domain.model.User

class AuthRequest(
    var login: String,
    var password: String,
)

class AuthResponse(
    var token: String,
    var id: String,
    var role: Role,
)

inline fun AuthResponse.toProfile(
    login: String,
    user: (String) -> User? = { null },
) = Profile(
    login,
    user(id),
    token,
    role = role
)