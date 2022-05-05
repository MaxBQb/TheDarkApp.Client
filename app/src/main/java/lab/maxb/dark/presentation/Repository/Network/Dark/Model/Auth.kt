package lab.maxb.dark.presentation.Repository.Network.Dark.Model

import lab.maxb.dark.Domain.Model.Role

class AuthRequest(
    var login: String,
    var password: String,
)

class AuthResponse(
    var token: String,
    var id: String,
    var role: Role,
)
