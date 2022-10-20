package lab.maxb.dark.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import lab.maxb.dark.domain.model.User
import lab.maxb.dark.domain.operations.randomUUID

@Entity(tableName = "user")
data class UserLocalDTO(
    val name: String,
    val rating: Int,

    @PrimaryKey
    override val id: String = randomUUID,
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
