package lab.maxb.dark.Presentation.Room.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import lab.maxb.dark.Domain.Model.User

@Entity(tableName = "user")
data class UserDTO(
    override var name: String,
    override var rating: Int,
    @PrimaryKey
    override var id: String,
): User(name=name, rating=rating) {
    constructor(user: User) : this(
        user.name,
        user.rating,
        user.id
    )
}
