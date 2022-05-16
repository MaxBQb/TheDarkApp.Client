package lab.maxb.dark.presentation.repository.utils

interface RefreshController {
    suspend fun isExpired(): Boolean
    suspend fun refresh()
}
