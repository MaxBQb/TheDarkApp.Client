package lab.maxb.dark.Presentation.Repository.Implementation

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import lab.maxb.dark.Domain.Model.User
import lab.maxb.dark.Presentation.Repository.Interfaces.UsersRepository
import lab.maxb.dark.Presentation.Repository.Room.DAO.UserDAO
import lab.maxb.dark.Presentation.Repository.Room.LocalDatabase
import lab.maxb.dark.Presentation.Repository.Room.Model.UserDTO

class UsersRepositoryImpl(db: LocalDatabase) : UsersRepository {
    private val mUserDao: UserDAO = db.userDao()

    override fun getUser(id: String)
        = mUserDao.getUser(id).distinctUntilChanged() as LiveData<User?>

    override suspend fun getUserOnce(id: String)
            = mUserDao.getUserOnce(id) as User?


    override suspend fun <T : User> addUser(user: T)
        = mUserDao.addUser(UserDTO(user))

    override suspend fun <T : User> deleteUser(user: T)
        = mUserDao.deleteUser(user as UserDTO)
}