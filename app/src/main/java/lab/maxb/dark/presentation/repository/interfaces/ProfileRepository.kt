package lab.maxb.dark.presentation.repository.interfaces

import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.domain.model.Profile

interface ProfileRepository {
    suspend fun sendCredentials(login: String, password: String, initial: Boolean = false)
    val profile: Flow<Profile?>
    suspend fun save(profile: Profile)
}
