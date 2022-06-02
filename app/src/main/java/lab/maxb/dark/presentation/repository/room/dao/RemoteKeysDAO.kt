package lab.maxb.dark.presentation.repository.room.dao

import androidx.room.Dao
import androidx.room.Query
import lab.maxb.dark.presentation.repository.room.model.RemoteKey

@Dao
abstract class RemoteKeysDAO: AdvancedDAO<RemoteKey>("remote_keys") {
    @Query("SELECT EXISTS(SELECT 1 FROM remote_keys)")
    abstract suspend fun hasContent(): Boolean
}