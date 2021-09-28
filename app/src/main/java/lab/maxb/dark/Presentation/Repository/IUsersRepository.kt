package lab.maxb.dark.Presentation.Repository

import androidx.lifecycle.LiveData
import lab.maxb.dark.Domain.Model.User

interface IUsersRepository {
    fun getUser(id: String): LiveData<User?>
    suspend fun <T : User> addUser(user: T)
    suspend fun <T : User> deleteUser(user: T)
}