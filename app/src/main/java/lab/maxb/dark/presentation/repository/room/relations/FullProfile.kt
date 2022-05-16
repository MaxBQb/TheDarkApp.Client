package lab.maxb.dark.presentation.repository.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import lab.maxb.dark.domain.model.Profile
import lab.maxb.dark.domain.model.User
import lab.maxb.dark.presentation.repository.room.Server.Model.ProfileDTO
import lab.maxb.dark.presentation.repository.room.model.UserDTO

data class FullProfile(
    @Embedded val profile: ProfileDTO,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "id",
        entity = UserDTO::class
    )
    val user: User?
) {
    fun toProfile() = profile.also {
        it.user = user
    } as Profile
}
