package lab.maxb.dark.data.local.model

import lab.maxb.dark.domain.model.Profile
import lab.maxb.dark.domain.model.Role

@kotlinx.serialization.Serializable
data class ProfileLocalDTO(
    val login: String,
    val userId: String,
    val token: String,
    val type: Profile.AuthType,
    val role: Role,
)

fun ProfileLocalDTO.toDomain() = Profile(
    login = login,
    userId = userId,
    token = token,
    type = type,
    role = role
)

fun Profile.toLocalDTO() = ProfileLocalDTO(
    login = login,
    userId = userId,
    token = token,
    type = type,
    role = role
)