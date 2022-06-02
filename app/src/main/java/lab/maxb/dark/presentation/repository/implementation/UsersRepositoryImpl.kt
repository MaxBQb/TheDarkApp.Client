package lab.maxb.dark.presentation.repository.implementation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import lab.maxb.dark.domain.model.User
import lab.maxb.dark.presentation.repository.interfaces.UsersRepository
import lab.maxb.dark.presentation.repository.network.dark.DarkService
import lab.maxb.dark.presentation.repository.room.LocalDatabase
import lab.maxb.dark.presentation.repository.room.dao.UsersDAO
import lab.maxb.dark.presentation.repository.room.model.toDomain
import lab.maxb.dark.presentation.repository.room.model.toLocalDTO
import lab.maxb.dark.presentation.repository.utils.Resource
import org.koin.core.annotation.Single

@Single
class UsersRepositoryImpl(
    db: LocalDatabase,
    private val networkDataSource: DarkService
) : UsersRepository {
    private val localDataSource: UsersDAO = db.users()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val userResource = Resource<String, User>().apply {
        fetchLocal = { localDataSource.get(it).mapLatest { x -> x?.toDomain() } }
        fetchRemote = { networkDataSource.getUser(it) }
        localStore = { localDataSource.save(it.toLocalDTO()) }
        clearLocalStore = { localDataSource.delete(it) }
    }

    override suspend fun getUser(id: String, fresh: Boolean): Flow<User?>
        = userResource.query(id, fresh, true)

    override suspend fun refresh(id: String) {
        userResource.refresh(id)
    }
}