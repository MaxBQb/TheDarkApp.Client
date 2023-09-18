package lab.maxb.dark.data.datasource.local

import lab.maxb.dark.data.model.local.RemoteKey

interface RemoteKeysLocalDataSource : BaseLocalDataSource<RemoteKey> {
    suspend fun hasContent(): Boolean
}
