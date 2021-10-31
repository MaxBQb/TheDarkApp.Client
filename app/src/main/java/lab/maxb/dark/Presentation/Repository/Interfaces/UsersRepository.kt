package lab.maxb.dark.Presentation.Repository.Interfaces

import androidx.lifecycle.LiveData
import lab.maxb.dark.Domain.Model.User

interface UsersRepository {
    fun getUser(id: String): LiveData<User?>
    suspend fun getUserOnce(id: String): User?
    suspend fun <T : User> addUser(user: T)
    suspend fun <T : User> deleteUser(user: T)
}