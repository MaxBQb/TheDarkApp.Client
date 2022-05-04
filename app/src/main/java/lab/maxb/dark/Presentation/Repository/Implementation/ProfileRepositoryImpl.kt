package lab.maxb.dark.Presentation.Repository.Implementation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import lab.maxb.dark.Domain.Model.Profile
import lab.maxb.dark.Presentation.Extra.UserSettings
import lab.maxb.dark.Presentation.Repository.Interfaces.ProfileRepository
import lab.maxb.dark.Presentation.Repository.Interfaces.UsersRepository
import lab.maxb.dark.Presentation.Repository.Network.Dark.DarkService
import lab.maxb.dark.Presentation.Repository.Network.Dark.Model.AuthRequest
import lab.maxb.dark.Presentation.Repository.Room.LocalDatabase
import lab.maxb.dark.Presentation.Repository.Room.Server.Model.ProfileDTO
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

    override suspend fun sendCredentials(login: String, password: String) {
        val response = darkService.login(AuthRequest(login, password))
        userSettings.token = response.token
        userSettings.login = login
        save(
            Profile(
                login,
                usersRepository.getUser(response.id).first(),
                response.token,
                role = response.role
            )
        )
        _login.value = login
    }

    private suspend fun checkToken(id: String) = try {
        usersRepository.getUser(id).first()!!
        true
    } catch (e: Throwable) {
        false
    }

    override suspend fun save(profile: Profile) = profileDAO.save(ProfileDTO(profile))
    override suspend fun clearCache() = profileDAO.clear()
}