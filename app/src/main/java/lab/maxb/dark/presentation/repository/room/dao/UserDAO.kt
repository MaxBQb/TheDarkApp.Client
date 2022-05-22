package lab.maxb.dark.presentation.repository.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.presentation.repository.room.model.UserDTO

@Dao
interface UserDAO {
    @Insert(onConflict = REPLACE)
    suspend fun addUser(user: UserDTO)

    @Query("DELETE FROM user WHERE id = :id")
    suspend fun deleteUser(id: String)

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUser(id: String): Flow<UserDTO?>
}