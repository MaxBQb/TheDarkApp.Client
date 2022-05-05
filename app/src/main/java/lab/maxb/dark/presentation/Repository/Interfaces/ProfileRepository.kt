package lab.maxb.dark.presentation.Repository.Interfaces

import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.Domain.Model.Profile

interface ProfileRepository {
    suspend fun sendCredentials(login: String, password: String, initial: Boolean = false)
    val profile: Flow<Profile?>
    suspend fun save(profile: Profile)
    suspend fun clearCache()
}
