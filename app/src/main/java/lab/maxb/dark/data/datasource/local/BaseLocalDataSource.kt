package lab.maxb.dark.data.datasource.local

import kotlinx.coroutines.flow.Flow

interface BaseLocalDataSource<T> {
    fun get(id: String): Flow<T?>
    suspend fun getById(id: String): T?
    suspend fun delete(id: String)
    suspend fun delete(vararg value: T)
    suspend fun clear()
    suspend fun save(value: T)
    suspend fun save(vararg value: T)
}