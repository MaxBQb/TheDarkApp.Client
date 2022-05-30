package lab.maxb.dark.presentation.repository.implementation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import lab.maxb.dark.domain.model.Profile
import lab.maxb.dark.presentation.extra.UserSettings
import lab.maxb.dark.presentation.repository.interfaces.ProfileRepository
import lab.maxb.dark.presentation.repository.interfaces.UsersRepository
import lab.maxb.dark.presentation.repository.network.dark.DarkService
import lab.maxb.dark.presentation.repository.network.dark.model.AuthRequest
import lab.maxb.dark.presentation.repository.room.LocalDatabase
import lab.maxb.dark.presentation.repository.room.model.toLocalDTO
import lab.maxb.dark.presentation.repository.room.relations.toDomain
import org.koin.core.annotation.Single

@Single
class ProfileRepositoryImpl(
    db: LocalDatabase,
    private val networkDataSource: DarkService,
    private val userSettings: UserSettings,
    private val usersRepository: UsersRepository,
) : ProfileRepository {
    private val localDataSource = db.profiles()
    private val _login = MutableStateFlow(userSettings.login)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val profile = _login.flatMapLatest { login ->
        localDataSource.getByLogin(login).distinctUntilChanged().mapLatest { fullProfile ->
            fullProfile?.toDomain()
        }
    }

    override suspend fun sendCredentials(login: String, password: String, initial: Boolean) {
        val request = AuthRequest(login, password)
        val response = if (initial)
            networkDataSource.signup(request)
        else
            networkDataSource.login(request)
        userSettings.token = response.token
        userSettings.login = login
        localDataSource.save(
            Profile(
                login,
                usersRepository.getUser(response.id).firstOrNull()!!,
                response.token,
                role = response.role
            ).toLocalDTO()
        )
        _login.value = login
    }
}