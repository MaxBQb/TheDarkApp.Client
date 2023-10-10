package lab.maxb.dark.data.repository

import lab.maxb.dark.data.local.datasource.UsersLocalDataSource
import lab.maxb.dark.data.local.model.toDomain
import lab.maxb.dark.data.local.model.toLocalDTO
import lab.maxb.dark.data.remote.datasource.UsersRemoteDataSource
import lab.maxb.dark.data.utils.DbRefreshController
import lab.maxb.dark.data.utils.ResourceImpl
import lab.maxb.dark.domain.repository.UsersRepository
import org.koin.core.annotation.Single

@Single
class UsersRepositoryImpl(
    private val remoteDataSource: UsersRemoteDataSource,
    private val localDataSource: UsersLocalDataSource,
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