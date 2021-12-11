package lab.maxb.dark.Presentation.Repository.Room.Relation

import androidx.room.Embedded
import androidx.room.Relation
import lab.maxb.dark.Domain.Model.Profile
import lab.maxb.dark.Domain.Model.User
import lab.maxb.dark.Presentation.Repository.Room.Model.UserDTO
import lab.maxb.dark.Presentation.Repository.Room.Server.Model.ProfileDTO

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
