package lab.maxb.dark.presentation.repository.interfaces

import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.domain.model.User

interface UsersRepository {
    fun getUser(id: String): Flow<User?>
    suspend fun clearCache()
}