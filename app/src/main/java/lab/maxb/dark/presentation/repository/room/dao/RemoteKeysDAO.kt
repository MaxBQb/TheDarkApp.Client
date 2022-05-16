package lab.maxb.dark.presentation.repository.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lab.maxb.dark.presentation.repository.room.model.RemoteKeys

@Dao
interface RemoteKeysDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remotekeys WHERE id = :id")
    suspend fun getById(id: String): RemoteKeys?

    @Query("DELETE FROM remotekeys")
    suspend fun clear()
}