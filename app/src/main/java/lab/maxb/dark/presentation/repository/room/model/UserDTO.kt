package lab.maxb.dark.presentation.repository.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import lab.maxb.dark.domain.model.User

@Entity(tableName = "user")
data class UserDTO(
    @PrimaryKey
    override var id: String,
    override var name: String,
    override var rating: Int,
): User(name=name, rating=rating) {
    constructor(user: User) : this(
        user.id,
        user.name,
        user.rating,
    )
}
