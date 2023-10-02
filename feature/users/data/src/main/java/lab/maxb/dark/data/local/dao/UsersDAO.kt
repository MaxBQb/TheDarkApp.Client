package lab.maxb.dark.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.data.local.model.UserLocalDTO


@Dao
abstract class UsersDAO : AdvancedDAO<UserLocalDTO>("user") {
    @Query("SELECT * FROM user WHERE id = :id")
    abstract fun get(id: String): Flow<UserLocalDTO?>
}