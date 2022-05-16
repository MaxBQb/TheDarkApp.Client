package lab.maxb.dark.presentation.repository.implementation

import lab.maxb.dark.domain.model.User
import lab.maxb.dark.presentation.repository.interfaces.UsersRepository
import lab.maxb.dark.presentation.repository.network.dark.DarkService
import lab.maxb.dark.presentation.repository.room.LocalDatabase
import lab.maxb.dark.presentation.repository.room.dao.UserDAO
import lab.maxb.dark.presentation.repository.room.model.UserDTO
import lab.maxb.dark.presentation.repository.utils.StaticResource
import org.koin.core.annotation.Single

@Single
class UsersRepositoryImpl(
    db: LocalDatabase,
    private val darkService: DarkService
) : UsersRepository {
    private val mUserDao: UserDAO = db.userDao()
    private val userResource = StaticResource<String, User>().apply {
        fetchLocal = { mUserDao.getUser(it) }
        fetchRemote = { darkService.getUser(it) }
        localStore = { mUserDao.addUser(UserDTO(it)) }
//        clearLocalStore = { mUserDao.deleteUser(it) }
    }

    override suspend fun getUser(id: String)
        = userResource.query(id)

    override suspend fun clearCache(): Unit = mUserDao.clear()
}