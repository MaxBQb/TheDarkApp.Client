package lab.maxb.dark.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import lab.maxb.dark.domain.model.User
import lab.maxb.dark.domain.operations.randomUUID

@Entity(tableName = "user")
data class UserLocalDTO(
    var name: String,
    var rating: Int,

    @PrimaryKey
    override var id: String = randomUUID,
): BaseLocalDTO()

fun User.toLocalDTO() = UserLocalDTO(
    name,
    rating,
    id,
)

fun UserLocalDTO.toDomain() = User(
    name,
    rating,
    id,
)
