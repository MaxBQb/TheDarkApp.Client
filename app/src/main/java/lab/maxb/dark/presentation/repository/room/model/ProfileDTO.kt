package lab.maxb.dark.presentation.repository.room.Server.Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import lab.maxb.dark.domain.model.Profile
import lab.maxb.dark.domain.model.Role
import lab.maxb.dark.presentation.repository.room.model.UserDTO

@Entity(tableName = "profile",
    ignoredColumns = ["user"],
    foreignKeys = [
        ForeignKey(
            entity = UserDTO::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProfileDTO(
    @PrimaryKey
    override var login: String,
    var user_id: String,
    override var token: String,
    override var type: AuthType,
    override var role: Role,
): Profile(login, null, token, type, role) {
    constructor(profile: Profile) : this(
        profile.login,
        profile.user!!.id,
        profile.token,
        profile.type,
        profile.role
    )
}
