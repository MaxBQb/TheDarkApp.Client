package lab.maxb.dark.Presentation.Repository.Room.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import lab.maxb.dark.Presentation.Repository.Room.Model.UserDTO

@Dao
interface UserDAO {
    @Insert(onConflict = IGNORE)
    suspend fun addUser(user: UserDTO)

    @Delete
    suspend fun deleteUser(user: UserDTO)

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUser(id: String): LiveData<UserDTO?>

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUserOnce(id: String): UserDTO?
}