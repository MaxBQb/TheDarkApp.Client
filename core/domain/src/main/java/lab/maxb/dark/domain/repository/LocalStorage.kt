package lab.maxb.dark.domain.repository

interface LocalStorage {
    suspend fun clear()
}