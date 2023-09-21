package lab.maxb.dark.data.model.remote

import lab.maxb.dark.domain.model.AuthCredentials
import lab.maxb.dark.domain.model.ReceivedAuthCredentials
import lab.maxb.dark.domain.model.Role

class AuthRequest(
    val login: String,
    val password: String,
)

class AuthResponse(
    val token: String,
    val id: String,
    val role: Role,
)

fun AuthCredentials.toNetworkDTO() = AuthRequest(
    login,
    password,
)

fun AuthResponse.toDomain(request: AuthCredentials) = ReceivedAuthCredentials(
    token, id, role, request,
)