package lab.maxb.dark.presentation.repository.room.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.*
import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.presentation.repository.room.model.UserDTO

@Dao
interface UserDAO {
    @Insert(onConflict = IGNORE)
    suspend fun addUser(user: UserDTO): Long

    @Update
    suspend fun updateUser(user: UserDTO)

    @Transaction
    suspend fun save(user: UserDTO) {
        if (addUser(user) == -1L)
            updateUser(user)
    }

    @Query("DELETE FROM user WHERE id = :id")
    suspend fun deleteUser(id: String)

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUser(id: String): Flow<UserDTO?>
}