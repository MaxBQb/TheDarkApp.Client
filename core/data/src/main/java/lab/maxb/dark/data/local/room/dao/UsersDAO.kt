package lab.maxb.dark.data.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.data.model.local.UserLocalDTO


@Dao
abstract class UsersDAO : AdvancedDAO<UserLocalDTO>("user") {
    @Query("SELECT * FROM user WHERE id = :id")
    abstract fun get(id: String): Flow<UserLocalDTO?>
}