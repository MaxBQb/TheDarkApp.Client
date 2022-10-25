package lab.maxb.dark.data.utils

interface RefreshController<T> {
    suspend fun isExpired(data: T?): Boolean
    suspend fun refresh()
}
