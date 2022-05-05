package lab.maxb.dark.presentation.repository.network.dark.model

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
