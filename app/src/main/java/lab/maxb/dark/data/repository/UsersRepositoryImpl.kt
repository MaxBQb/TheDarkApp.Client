package lab.maxb.dark.data.repository

import lab.maxb.dark.data.local.room.LocalDatabase
import lab.maxb.dark.data.local.room.dao.UsersDAO
import lab.maxb.dark.data.model.local.UserLocalDTO
import lab.maxb.dark.data.model.local.toDomain
import lab.maxb.dark.data.model.local.toLocalDTO
import lab.maxb.dark.data.remote.dark.DarkService
import lab.maxb.dark.data.utils.DbRefreshController
import lab.maxb.dark.data.utils.ResourceImpl
import lab.maxb.dark.domain.model.User
import lab.maxb.dark.domain.repository.UsersRepository
import org.koin.core.annotation.Single

@Single
class UsersRepositoryImpl(
    db: LocalDatabase,
    private val networkDataSource: DarkService
) : UsersRepository {
    private val localDataSource: UsersDAO = db.users()

    override val userResource = ResourceImpl<String, User, UserLocalDTO>(
        refreshController = DbRefreshController(),
        fetchLocal = { localDataSource.get(it) },
        localMapper = { it?.toDomain() },
        fetchRemote = { networkDataSource.getUser(it) },
        localStore = { localDataSource.save(it.toLocalDTO()) },
        clearLocalStore = { localDataSource.delete(it) },
    )
}