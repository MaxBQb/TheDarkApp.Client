package lab.maxb.dark.data.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.data.local.room.relations.FullProfileLocalDTO
import lab.maxb.dark.data.model.local.ProfileLocalDTO

@Dao
abstract class ProfilesDAO: AdvancedDAO<ProfileLocalDTO>("profile") {
    @Query("SELECT * FROM profile WHERE id = :login")
    abstract fun getByLogin(login: String): Flow<FullProfileLocalDTO?>

    @Query("SELECT user_id FROM profile WHERE id = :login")
    abstract suspend fun getUserIdByLogin(login: String): String?
}