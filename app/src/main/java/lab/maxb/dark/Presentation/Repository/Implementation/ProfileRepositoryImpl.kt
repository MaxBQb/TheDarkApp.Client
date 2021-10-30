package lab.maxb.dark.Presentation.Repository.Implementation

import lab.maxb.dark.Domain.Model.Server.Profile
import lab.maxb.dark.Presentation.Repository.Interfaces.ProfileRepository
import lab.maxb.dark.Presentation.Repository.Room.LocalDatabase
import lab.maxb.dark.Presentation.Repository.Room.Server.Model.ProfileDTO

class ProfileRepositoryImpl(db: LocalDatabase) : ProfileRepository {
    private val mProfileDao = db.profileDao()

    override suspend fun getProfile(name: String, hash: String): Profile?
        = mProfileDao.getProfile(name, hash)

    override suspend fun addProfile(profile: Profile)
        = mProfileDao.addProfile(ProfileDTO(profile))
}