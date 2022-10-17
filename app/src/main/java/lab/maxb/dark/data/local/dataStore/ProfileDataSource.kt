package lab.maxb.dark.data.local.dataStore

import androidx.datastore.core.DataStore
import lab.maxb.dark.domain.model.Profile

interface ProfileDataSource : DataStore<Profile?> {
    suspend fun clear()
}

