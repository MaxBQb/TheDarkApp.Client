package lab.maxb.dark.data.local.datasource

import lab.maxb.dark.data.local.model.RemoteKey

interface RemoteKeysLocalDataSource : BaseLocalDataSource<RemoteKey> {
    suspend fun hasContent(): Boolean
}
