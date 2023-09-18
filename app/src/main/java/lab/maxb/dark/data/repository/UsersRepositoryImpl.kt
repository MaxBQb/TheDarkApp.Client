package lab.maxb.dark.data.repository

import lab.maxb.dark.data.datasource.UsersRemoteDataSource
import lab.maxb.dark.data.local.room.dao.UsersDAO
import lab.maxb.dark.data.model.local.toDomain
import lab.maxb.dark.data.model.local.toLocalDTO
import lab.maxb.dark.data.utils.DbRefreshController
import lab.maxb.dark.data.utils.ResourceImpl
import lab.maxb.dark.domain.repository.UsersRepository
import org.koin.core.annotation.Single

@Single
class UsersRepositoryImpl(
    private val remoteDataSource: UsersRemoteDataSource,
    private val localDataSource: UsersDAO,
) : UsersRepository {

    override val userResource = ResourceImpl(
        refreshController = DbRefreshController(),
        fetchLocal = localDataSource::get,
        localMapper = { it?.toDomain() },
        reversedLocalMapper = { it.toLocalDTO() },
        fetchRemote = remoteDataSource::getUser,
        localStore = localDataSource::save,
        clearLocalStore = localDataSource::delete,
    )
}