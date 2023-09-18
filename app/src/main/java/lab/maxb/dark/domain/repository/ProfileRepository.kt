package lab.maxb.dark.domain.repository

import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.domain.model.AuthCredentials
import lab.maxb.dark.domain.model.Profile
import lab.maxb.dark.domain.repository.utils.Resource

interface ProfileRepository {
    val profileResource: Resource<AuthCredentials?, Profile>
    suspend fun sendCredentials(credentials: AuthCredentials): Profile?
    suspend fun clear()
    val profile: Flow<Profile?>
    val isTokenExpired: Flow<Boolean>
}
