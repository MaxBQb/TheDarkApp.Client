package lab.maxb.dark.data.model.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import lab.maxb.dark.domain.model.Profile
import lab.maxb.dark.domain.model.Role
import lab.maxb.dark.domain.operations.randomUUID

@Entity(tableName = "profile",
    foreignKeys = [
        ForeignKey(
            entity = UserLocalDTO::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProfileLocalDTO(
    var user_id: String,
    var token: String,
    var type: Profile.AuthType,
    var role: Role,

    @PrimaryKey
    override var id: String = randomUUID,
): BaseLocalDTO()

fun Profile.toLocalDTO() = ProfileLocalDTO(
    user!!.id,
    token,
    type,
    role,
    login,
)
