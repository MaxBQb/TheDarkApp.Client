package lab.maxb.dark.data.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import lab.maxb.dark.data.local.room.LocalDatabase
import lab.maxb.dark.data.local.room.relations.toDomain
import lab.maxb.dark.data.model.local.toLocalDTO
import lab.maxb.dark.data.model.remote.toDomain
import lab.maxb.dark.data.model.remote.toNetworkDTO
import lab.maxb.dark.data.remote.dark.DarkService
import lab.maxb.dark.data.utils.RefreshControllerImpl
import lab.maxb.dark.data.utils.Resource
import lab.maxb.dark.domain.model.AuthCredentials
import lab.maxb.dark.domain.model.Profile
import lab.maxb.dark.domain.operations.toProfile
import lab.maxb.dark.domain.repository.ProfileRepository
import lab.maxb.dark.domain.repository.UserSettings
import lab.maxb.dark.domain.repository.UsersRepository
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