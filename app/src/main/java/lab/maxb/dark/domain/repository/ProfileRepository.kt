package lab.maxb.dark.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import lab.maxb.dark.domain.model.AuthCredentials
import lab.maxb.dark.domain.model.Profile

interface ProfileRepository {
    suspend fun sendCredentials(credentials: AuthCredentials): Profile?
    suspend fun retry(): Boolean
    val profile: Flow<Profile?>
    val isTokenExpired: StateFlow<Boolean>
}
