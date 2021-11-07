package lab.maxb.dark.Presentation.Repository.Room.Server.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import lab.maxb.dark.Presentation.Repository.Room.Server.Model.ProfileDTO

@Dao
interface ProfileDAO {
    @Insert(onConflict = IGNORE)
    suspend fun addProfile(profile: ProfileDTO)

    @Query("SELECT * FROM profile WHERE login = :login AND hash = :hash")
    suspend fun getProfile(login: String, hash: String): ProfileDTO?
}