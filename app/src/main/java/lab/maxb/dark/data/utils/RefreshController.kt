package lab.maxb.dark.data.utils

interface RefreshController {
    suspend fun isExpired(): Boolean
    suspend fun refresh()
}
