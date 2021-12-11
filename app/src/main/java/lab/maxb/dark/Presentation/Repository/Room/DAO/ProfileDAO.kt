package lab.maxb.dark.Presentation.Repository.Room.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.Presentation.Repository.Room.Relation.FullProfile
import lab.maxb.dark.Presentation.Repository.Room.Server.Model.ProfileDTO3

@Dao
interface ProfileDAO3 {
    @Insert(onConflict = REPLACE)
    suspend fun save(profile: ProfileDTO3)

    @Query("SELECT * FROM profile WHERE login = :login")
    fun getByLogin(login: String): Flow<FullProfile?>
}