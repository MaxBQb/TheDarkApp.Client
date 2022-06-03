package lab.maxb.dark.presentation.repository.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.presentation.repository.room.model.UserLocalDTO


@Dao
abstract class UsersDAO : AdvancedDAO<UserLocalDTO>("user") {
    @Query("SELECT * FROM user WHERE id = :id")
    abstract fun get(id: String): Flow<UserLocalDTO?>
}