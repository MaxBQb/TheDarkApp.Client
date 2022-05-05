package lab.maxb.dark.presentation.Repository.Room.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import lab.maxb.dark.Domain.Model.User

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
