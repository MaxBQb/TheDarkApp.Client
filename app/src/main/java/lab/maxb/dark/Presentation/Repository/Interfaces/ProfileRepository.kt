package lab.maxb.dark.Presentation.Repository.Interfaces

import lab.maxb.dark.Domain.Model.Server.Profile

interface ProfileRepository {
    suspend fun getProfile(login: String, hash: String): Profile?

    suspend fun addProfile(profile: Profile)
}