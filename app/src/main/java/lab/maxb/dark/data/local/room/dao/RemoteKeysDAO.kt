package lab.maxb.dark.data.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import lab.maxb.dark.data.model.local.RemoteKey

@Dao
abstract class RemoteKeysDAO: AdvancedDAO<RemoteKey>("remote_keys") {
    @Query("SELECT EXISTS(SELECT 1 FROM remote_keys)")
    abstract suspend fun hasContent(): Boolean
}