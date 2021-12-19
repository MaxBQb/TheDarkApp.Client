package lab.maxb.dark.Presentation.Repository.Interfaces

import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.Domain.Model.User

interface UsersRepository {
    fun getUser(id: String): Flow<User?>
    suspend fun clearCache()
}