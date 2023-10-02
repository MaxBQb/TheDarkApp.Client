package lab.maxb.dark.data.local.datasource

import androidx.datastore.core.DataStore

abstract class BaseDataStoreDataSource<T>(
    protected val dao: DataStore<T>
): BaseLocalUniqueDataSource<T> {
    override val data get() = dao.data
    override suspend fun update(transform: suspend (T) -> T) {
        dao.updateData(transform)
    }
}