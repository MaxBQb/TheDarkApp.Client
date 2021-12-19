package lab.maxb.dark.Presentation.Repository.Implementation

import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import lab.maxb.dark.Domain.Model.User
import lab.maxb.dark.Presentation.Repository.Interfaces.UsersRepository
import lab.maxb.dark.Presentation.Repository.Network.Dark.DarkService
import lab.maxb.dark.Presentation.Repository.Room.DAO.UserDAO
import lab.maxb.dark.Presentation.Repository.Room.LocalDatabase
import lab.maxb.dark.Presentation.Repository.Room.Model.UserDTO

class UsersRepositoryImpl(
    db: LocalDatabase,
    private val darkService: DarkService
) : UsersRepository {
    private val mUserDao: UserDAO = db.userDao()

    override fun getUser(id: String) = flow {
        if (mUserDao.hasUser(id)) {
            emitAll(mUserDao.getUser(id).distinctUntilChanged())
            refreshUser(id)
        } else {
            refreshUser(id)
            emitAll(mUserDao.getUser(id).distinctUntilChanged())
        }
    }

    private suspend fun refreshUser(id: String) = try {
        addUser(darkService.getUser(id)!!)
    } catch (e: Throwable) {
        e.printStackTrace()
    }

    suspend fun <T : User> addUser(user: T)
        = mUserDao.addUser(UserDTO(user))

    override suspend fun clearCache(): Unit = mUserDao.clear()
}