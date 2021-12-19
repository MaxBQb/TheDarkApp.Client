package lab.maxb.dark.Presentation.Repository.Interfaces

import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.Domain.Model.Profile

interface ProfileRepository {
    suspend fun getProfile(login: String? = null, password: String? = null): Flow<Profile?>
    val profile: Profile?
    suspend fun save(profile: Profile)
    suspend fun clearCache()
}
