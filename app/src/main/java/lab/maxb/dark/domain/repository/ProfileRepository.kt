package lab.maxb.dark.domain.repository

import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.domain.model.AuthCredentials
import lab.maxb.dark.domain.model.Profile

interface ProfileRepository {
    fun sendCredentials(credentials: AuthCredentials)
    val profile: Flow<Profile?>
}
