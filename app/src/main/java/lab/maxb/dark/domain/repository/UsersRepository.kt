package lab.maxb.dark.domain.repository

import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.domain.model.User

interface UsersRepository {
    suspend fun getUser(id: String, fresh: Boolean = false): Flow<User?>
    suspend fun refresh(id: String): Boolean
}