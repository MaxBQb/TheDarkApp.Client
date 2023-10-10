package lab.maxb.dark.data.local.model

import androidx.room.Embedded
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

    @Embedded
    val time: TimeContainerImpl = TimeContainerImpl(),
): BaseLocalDTO(), TimeContainer by time

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