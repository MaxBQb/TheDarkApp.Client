package lab.maxb.dark.Presentation.Repository.Room.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.Presentation.Repository.Room.Relation.FullProfile
import lab.maxb.dark.Presentation.Repository.Room.Server.Model.ProfileDTO

@Dao
interface ProfileDAO {
    @Insert(onConflict = REPLACE)
    suspend fun save(profile: ProfileDTO)

    @Query("SELECT * FROM profile WHERE login = :login")
    fun getByLogin(login: String): Flow<FullProfile?>

    @Transaction
    @Query("DELETE FROM profile")
    fun clear()
}