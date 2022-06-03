package lab.maxb.dark.presentation.repository.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import lab.maxb.dark.domain.model.Profile
import lab.maxb.dark.domain.model.User
import lab.maxb.dark.presentation.repository.room.model.ProfileLocalDTO
import lab.maxb.dark.presentation.repository.room.model.UserLocalDTO

data class FullProfileLocalDTO(
    @Embedded val profile: ProfileLocalDTO,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "id",
        entity = UserLocalDTO::class
    )
    val user: User?
)

fun FullProfileLocalDTO.toDomain() = Profile(
    profile.id,
    user,
    profile.token,
    profile.type,
    profile.role,
)
