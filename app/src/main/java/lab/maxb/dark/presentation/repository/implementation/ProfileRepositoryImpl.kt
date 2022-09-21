package lab.maxb.dark.presentation.repository.implementation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import lab.maxb.dark.domain.model.AuthCredentials
import lab.maxb.dark.domain.model.Profile
import lab.maxb.dark.domain.operations.toProfile
import lab.maxb.dark.presentation.extra.UserSettings
import lab.maxb.dark.presentation.repository.interfaces.ProfileRepository
import lab.maxb.dark.presentation.repository.interfaces.UsersRepository
import lab.maxb.dark.presentation.repository.network.dark.DarkService
import lab.maxb.dark.presentation.repository.network.dark.model.toDomain
import lab.maxb.dark.presentation.repository.network.dark.model.toNetworkDTO
import lab.maxb.dark.presentation.repository.room.LocalDatabase
import lab.maxb.dark.presentation.repository.room.model.toLocalDTO
import lab.maxb.dark.presentation.repository.room.relations.toDomain
import lab.maxb.dark.presentation.repository.utils.RefreshControllerImpl
import lab.maxb.dark.presentation.repository.utils.Resource
import org.koin.core.annotation.Single
import java.time.Duration

@Single
class ProfileRepositoryImpl(
    db: LocalDatabase,
    private val networkDataSource: DarkService,
    private val userSettings: UserSettings,
    private val usersRepository: UsersRepository,
) : ProfileRepository {
    private val localDataSource = db.profiles()
    private val _credentials = MutableSharedFlow<AuthCredentials>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    init {
        _credentials.tryEmit(AuthCredentials(userSettings.login))
        networkDataSource.onAuthRequired = {
            userSettings.login = ""
            userSettings.token = ""
            localDataSource.clear()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val profile = _credentials.flatMapLatest {
        profileResource.query(it, true)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val profileResource = Resource<AuthCredentials, Profile>(
        RefreshControllerImpl(Duration.ofHours(12).toMillis())
    ).apply {
        fetchLocal = {
            localDataSource.getByLogin(it.login).mapLatest { x -> x?.toDomain() }
        }
        fetchRemote = remote@ {
            it.password ?: run {
                localDataSource.getUserIdByLogin(it.login)?.let { id ->
                    usersRepository.refresh(id)
                }
                return@remote null
            }

            val request = it.toNetworkDTO()
            val response = (if (it.initial)
                networkDataSource.signup(request)
            else
                networkDataSource.login(request)).toDomain(it)
            userSettings.token = response.token
            userSettings.login = it.login
            response.toProfile { id ->
                usersRepository.getUser(id).firstOrNull() ?: return@remote null
            }
        }
        localStore = { localDataSource.save(it.toLocalDTO()) }
    }

    override fun sendCredentials(credentials: AuthCredentials) {
        _credentials.tryEmit(credentials)
    }
}