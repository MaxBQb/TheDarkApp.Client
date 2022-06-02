package lab.maxb.dark.presentation.repository.implementation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
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
    private val _credentials = MutableStateFlow(AuthCredentials(userSettings.login))

    init {
        networkDataSource.onAuthRequired = {
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
                    usersRepository.getUser(id).firstOrNull()
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
        _credentials.value = credentials
    }
}