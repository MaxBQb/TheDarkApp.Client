package lab.maxb.dark.Presentation.Repository.Interfaces

import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.Domain.Model.User

interface UsersRepository {
    suspend fun getUser(id: String): Flow<User?>
    suspend fun getUserOnce(id: String): User?
    suspend fun <T : User> addUser(user: T)
    suspend fun <T : User> deleteUser(user: T)
}