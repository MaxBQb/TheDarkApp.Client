package lab.maxb.dark.Presentation.Repository.Room.Server.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import lab.maxb.dark.Domain.Model.Role
import lab.maxb.dark.Domain.Model.Server.Profile

@Entity(tableName = "profile")
data class ProfileDTO(
    @PrimaryKey
    override var id: String,
    override var name: String,
    override var rating: Int,
    override var hash: String?,
    override var role: Role,
): Profile(name=name, rating=rating, hash=hash, role=role) {
    constructor(profile: Profile) : this(
        profile.id,
        profile.name,
        profile.rating,
        profile.hash,
        profile.role,
    )
}
