package lab.maxb.dark.Presentation.Repository.Implementation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest
import lab.maxb.dark.Domain.Model.Profile
import lab.maxb.dark.Presentation.Extra.SessionHolder
import lab.maxb.dark.Presentation.Repository.Interfaces.ProfileRepository
import lab.maxb.dark.Presentation.Repository.Interfaces.UsersRepository
import lab.maxb.dark.Presentation.Repository.Network.Dark.DarkService
import lab.maxb.dark.Presentation.Repository.Network.Dark.Model.AuthRequest
import lab.maxb.dark.Presentation.Repository.Room.LocalDatabase
import lab.maxb.dark.Presentation.Repository.Room.Server.Model.ProfileDTO

class ProfileRepositoryImpl(
    db: LocalDatabase,
    private val darkService: DarkService,
    private val sessionHolder: SessionHolder,
    private val usersRepository: UsersRepository,
) : ProfileRepository {
    private val profileDAO = db.profileDao()
    override var profile: Profile? = null

    override suspend fun getProfile(login: String?, password: String?): Flow<Profile?> {
        val login = (login ?: sessionHolder.login)!!
        password?.let {
            val response = darkService.login(AuthRequest(
                login, it
            ))
            sessionHolder.token = response.token
            sessionHolder.login = login

            save(Profile(
                login,
                usersRepository.getUser(response.id).first(),
                response.token,
                role=response.role
            ).also { profile = it })
        }
        return profileDAO.getByLogin(login).map {
            it?.toProfile()?.also { profile = it }
        }.also { it.first()?.user?.id?.let{ id ->
            assert(checkToken(id))
        } }
    }

    private suspend fun checkToken(id: String) = try {
        usersRepository.getUser(id)
        true
    } catch (e: Throwable) { false }

    override suspend fun save(profile: Profile)
        = profileDAO.save(ProfileDTO(profile))
}