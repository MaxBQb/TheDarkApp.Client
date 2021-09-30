package lab.maxb.dark.Presentation.Repository

import android.content.Context
import lab.maxb.dark.Presentation.Repository.Room.LocalDatabase.Companion.getDatabase
import androidx.lifecycle.LiveData
import lab.maxb.dark.Domain.Model.User
import lab.maxb.dark.Presentation.Repository.Room.DAO.UserDAO
import lab.maxb.dark.Presentation.Repository.Room.Model.UserDTO

class UsersRepository(applicationContext: Context) : IUsersRepository {
    private val mUserDao: UserDAO

    init {
        val db = getDatabase(applicationContext)
        mUserDao = db.userDao()
    }

    override fun getUser(id: String)
        = mUserDao.getUser(id) as LiveData<User?>

    override suspend fun <T : User> addUser(user: T)
        = mUserDao.addUser(UserDTO(user))

    override suspend fun <T : User> deleteUser(user: T)
        = mUserDao.deleteUser(user as UserDTO)
}