package lab.maxb.dark.presentation.repository.implementation

import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import lab.maxb.dark.domain.model.User
import lab.maxb.dark.presentation.repository.interfaces.UsersRepository
import lab.maxb.dark.presentation.repository.network.dark.DarkService
import lab.maxb.dark.presentation.repository.room.dao.UserDAO
import lab.maxb.dark.presentation.repository.room.LocalDatabase
import lab.maxb.dark.presentation.repository.room.model.UserDTO
import org.koin.core.annotation.Single

@Single
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