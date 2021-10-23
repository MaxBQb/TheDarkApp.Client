package lab.maxb.dark.Presentation.Repository.Implementation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import lab.maxb.dark.Domain.Model.User
import lab.maxb.dark.Presentation.Repository.Interfaces.UsersRepository
import lab.maxb.dark.Presentation.Repository.Room.DAO.UserDAO
import lab.maxb.dark.Presentation.Repository.Room.LocalDatabase.Companion.getDatabase
import lab.maxb.dark.Presentation.Repository.Room.Model.UserDTO

class UsersRepositoryImpl(applicationContext: Context) : UsersRepository {
    private val mUserDao: UserDAO

    init {
        val db = getDatabase(applicationContext)
        mUserDao = db.userDao()
    }

    override fun getUser(id: String)
        = mUserDao.getUser(id).distinctUntilChanged() as LiveData<User?>

    override suspend fun <T : User> addUser(user: T)
        = mUserDao.addUser(UserDTO(user))

    override suspend fun <T : User> deleteUser(user: T)
        = mUserDao.deleteUser(user as UserDTO)
}