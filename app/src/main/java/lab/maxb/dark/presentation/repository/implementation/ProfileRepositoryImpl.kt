package lab.maxb.dark.presentation.repository.implementation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import lab.maxb.dark.domain.model.Profile
import lab.maxb.dark.presentation.extra.UserSettings
import lab.maxb.dark.presentation.repository.interfaces.ProfileRepository
import lab.maxb.dark.presentation.repository.interfaces.UsersRepository
import lab.maxb.dark.presentation.repository.network.dark.DarkService
import lab.maxb.dark.presentation.repository.network.dark.model.AuthRequest
import lab.maxb.dark.presentation.repository.room.LocalDatabase
import lab.maxb.dark.presentation.repository.room.Server.Model.ProfileDTO
import org.koin.core.annotation.Single

@Single
class ProfileRepositoryImpl(
    db: LocalDatabase,
    private val darkService: DarkService,
    private val userSettings: UserSettings,
    private val usersRepository: UsersRepository,
) : ProfileRepository {
    private val profileDAO = db.profileDao()
    private val _login = MutableStateFlow(userSettings.login)

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    override val profile = _login.flatMapLatest { login ->
        profileDAO.getByLogin(login).distinctUntilChanged().mapLatest { fullProfile ->
            fullProfile?.toProfile()?.also {
                it.user?.id?.let { id ->
                    assert(checkToken(id))
                }
            }
        }
    }

    override suspend fun sendCredentials(login: String, password: String, initial: Boolean) {
        val request = AuthRequest(login, password)
        val response = if (initial)
            darkService.signup(request)
        else
            darkService.login(request)
        userSettings.token = response.token
        userSettings.login = login
        save(
            Profile(
                login,
                usersRepository.getUser(response.id).firstOrNull()!!,
                response.token,
                role = response.role
            )
        )
        _login.value = login
    }

    private suspend fun checkToken(id: String) = try {
        usersRepository.getUser(id).firstOrNull()!!
        true
    } catch (e: Throwable) {
        false
    }

    override suspend fun save(profile: Profile) = profileDAO.save(ProfileDTO(profile))
}