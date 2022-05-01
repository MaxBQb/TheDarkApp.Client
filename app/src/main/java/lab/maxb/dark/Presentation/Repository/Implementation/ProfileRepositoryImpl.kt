package lab.maxb.dark.Presentation.Repository.Implementation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
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
    override var profile: Profile? = null

    override suspend fun getProfile(login: String?, password: String?): Flow<Profile?> {
        val login = login ?: userSettings.login
        password?.let {
            val response = darkService.login(AuthRequest(
                login, it
            ))

            userSettings.token = response.token
            userSettings.login = login
            try {
                save(Profile(
                    login,
                    usersRepository.getUser(response.id).first(),
                    response.token,
                    role = response.role
                ).also { profile = it })
            } catch (e: Throwable) {
                userSettings.token = ""
                userSettings.login = ""
                throw e
            }
        }
        return profileDAO.getByLogin(login).map {
            it?.toProfile()?.also { profile = it }
        }.also { it.first()?.user?.id?.let { id ->
            assert(checkToken(id))
        } }
    }

    private suspend fun checkToken(id: String) = try {
        usersRepository.getUser(id).first()!!
        true
    } catch (e: Throwable) { false }

    override suspend fun save(profile: Profile)
        = profileDAO.save(ProfileDTO(profile))

    override suspend fun clearCache(): Unit = profileDAO.clear()
}