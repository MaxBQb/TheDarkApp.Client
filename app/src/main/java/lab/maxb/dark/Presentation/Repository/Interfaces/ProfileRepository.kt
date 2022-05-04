package lab.maxb.dark.Presentation.Repository.Interfaces

import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.Domain.Model.Profile

interface ProfileRepository {
    suspend fun sendCredentials(login: String, password: String)
    val profile: Flow<Profile?>
    suspend fun save(profile: Profile)
    suspend fun clearCache()
}
