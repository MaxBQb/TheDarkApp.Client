package lab.maxb.dark.Presentation.Repository.Implementation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import lab.maxb.dark.Domain.Model.Profile3
import lab.maxb.dark.Presentation.Extra.SessionHolder
import lab.maxb.dark.Presentation.Repository.Interfaces.ProfileRepository
import lab.maxb.dark.Presentation.Repository.Interfaces.UsersRepository
import lab.maxb.dark.Presentation.Repository.Network.Dark.DarkService
import lab.maxb.dark.Presentation.Repository.Network.Dark.Model.AuthRequest
import lab.maxb.dark.Presentation.Repository.Room.LocalDatabase
import lab.maxb.dark.Presentation.Repository.Room.Server.Model.ProfileDTO3

class ProfileRepositoryImpl(
    db: LocalDatabase,
    private val darkService: DarkService,
    private val sessionHolder: SessionHolder,
    private val usersRepository: UsersRepository,
) : ProfileRepository {
    private val profileDAO = db.profileDao()
    override var profile: Profile3? = null

    override suspend fun getProfile(login: String?, password: String?): Flow<Profile3?> {
        val login = (login ?: sessionHolder.login)!!
        password?.let {
            val response = darkService.login(AuthRequest(
                login, it
            ))
            sessionHolder.token = response.token
            sessionHolder.login = login

            save(Profile3(
                login,
                usersRepository.getUser(response.id).first(),
                response.token,
                role=response.role
            ).also { profile = it })
        }
        return profileDAO.getByLogin(login)
            .map { it?.toProfile() }
    }

    override suspend fun save(profile: Profile3)
        = profileDAO.save(ProfileDTO3(profile))
}