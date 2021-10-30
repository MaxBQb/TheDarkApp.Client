package lab.maxb.dark.Presentation.Repository.Room.Server.Relation

import androidx.room.Embedded
import androidx.room.Relation
import lab.maxb.dark.Domain.Model.Server.Profile
import lab.maxb.dark.Domain.Model.Server.Session
import lab.maxb.dark.Presentation.Repository.Room.Server.Model.ProfileDTO
import lab.maxb.dark.Presentation.Repository.Room.Server.Model.SessionDTO

data class SessionWithProfile(
    @Embedded val session: SessionDTO,
    @Relation(
        parentColumn = "profile_id",
        entityColumn = "id",
        entity = ProfileDTO::class
    )
    val profile: ProfileDTO?,
) {
    fun toSession() = session.also {
        it.profile = profile as Profile
    } as Session
}
