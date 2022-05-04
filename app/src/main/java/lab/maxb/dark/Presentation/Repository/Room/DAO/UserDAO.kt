package lab.maxb.dark.Presentation.Repository.Room.DAO

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.Presentation.Repository.Room.Model.UserDTO

@Dao
interface UserDAO {
    @Insert(onConflict = REPLACE)
    suspend fun addUser(user: UserDTO)

    @Delete
    suspend fun deleteUser(user: UserDTO)

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUser(id: String): Flow<UserDTO?>

    @Query("SELECT EXISTS(SELECT * FROM user WHERE id = :id)")
    suspend fun hasUser(id: String): Boolean

    @Transaction
    @Query("DELETE FROM user")
    suspend fun clear()
}