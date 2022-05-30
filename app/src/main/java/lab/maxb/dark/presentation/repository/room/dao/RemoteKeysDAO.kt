package lab.maxb.dark.presentation.repository.room.dao

import androidx.room.Dao
import lab.maxb.dark.presentation.repository.room.model.RemoteKey

@Dao
abstract class RemoteKeysDAO: AdvancedDAO<RemoteKey>("remote_keys")