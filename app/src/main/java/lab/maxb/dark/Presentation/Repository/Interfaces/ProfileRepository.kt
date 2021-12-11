package lab.maxb.dark.Presentation.Repository.Interfaces

import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.Domain.Model.Profile3

interface ProfileRepository {
    suspend fun getProfile(login: String? = null, password: String? = null): Flow<Profile3?>
    val profile: Profile3?
    suspend fun save(profile: Profile3)
}
