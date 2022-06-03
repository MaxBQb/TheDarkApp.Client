package lab.maxb.dark.presentation.repository.network.dark.model

import lab.maxb.dark.domain.model.AuthCredentials
import lab.maxb.dark.domain.model.ReceivedAuthCredentials
import lab.maxb.dark.domain.model.Role

class AuthRequest(
    var login: String,
    var password: String,
)

class AuthResponse(
    var token: String,
    var id: String,
    var role: Role,
)

fun AuthCredentials.toNetworkDTO() = AuthRequest(
    login,
    password!!,
)

fun AuthResponse.toDomain(request: AuthCredentials) = ReceivedAuthCredentials(
    token, id, role, request,
)