package lab.maxb.dark.presentation.repository.implementation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import lab.maxb.dark.domain.model.Profile
import lab.maxb.dark.domain.model.User
import lab.maxb.dark.presentation.extra.UserSettings
import lab.maxb.dark.presentation.repository.interfaces.ProfileRepository
import lab.maxb.dark.presentation.repository.interfaces.UsersRepository
import lab.maxb.dark.presentation.repository.network.dark.DarkService
import lab.maxb.dark.presentation.repository.network.dark.model.AuthRequest
import lab.maxb.dark.presentation.repository.network.dark.model.toProfile
import lab.maxb.dark.presentation.repository.room.LocalDatabase
import lab.maxb.dark.presentation.repository.room.model.toDomain
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
    private val _credentials = MutableStateFlow(Credentials(
        userSettings.login,
    ))

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
    private val profileResource = Resource<Credentials, Profile>(
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

            val request = it.toRequest()
            val response = if (it.initial)
                networkDataSource.signup(request)
            else
                networkDataSource.login(request)
            userSettings.token = response.token
            userSettings.login = it.login
            response.toProfile(it.login) { id ->
                usersRepository.getUser(id).firstOrNull() ?: return@remote null
            }
        }
        localStore = { localDataSource.save(it.toLocalDTO()) }
    }

    override suspend fun sendCredentials(login: String, password: String, initial: Boolean) {
        _credentials.value = Credentials(login, password, initial)
    }

    private data class Credentials(
        val login: String,
        val password: String? = null,
        val initial: Boolean = false,
    )

    private fun Credentials.toRequest() = AuthRequest(login, password!!)
}