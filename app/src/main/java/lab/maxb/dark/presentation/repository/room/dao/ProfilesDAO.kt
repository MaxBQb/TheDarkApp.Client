package lab.maxb.dark.presentation.repository.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.presentation.repository.room.model.ProfileLocalDTO
import lab.maxb.dark.presentation.repository.room.relations.FullProfileLocalDTO

@Dao
abstract class ProfilesDAO: AdvancedDAO<ProfileLocalDTO>("profile") {
    @Query("SELECT * FROM profile WHERE id = :login")
    abstract fun getByLogin(login: String): Flow<FullProfileLocalDTO?>
}