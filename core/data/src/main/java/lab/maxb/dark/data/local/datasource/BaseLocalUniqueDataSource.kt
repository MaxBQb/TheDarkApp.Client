package lab.maxb.dark.data.local.datasource

import kotlinx.coroutines.flow.Flow

interface BaseLocalUniqueDataSource<T> {
    val data: Flow<T>
    suspend fun update(transform: suspend (T) -> T)
}

suspend fun <T> BaseLocalUniqueDataSource<T>.save(model: T) = update { model }
suspend fun <T> BaseLocalUniqueDataSource<T?>.clear() = update { null }