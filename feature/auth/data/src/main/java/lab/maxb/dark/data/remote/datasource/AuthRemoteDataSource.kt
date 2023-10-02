package lab.maxb.dark.data.remote.datasource

import kotlinx.coroutines.flow.SharedFlow
import lab.maxb.dark.data.remote.model.AuthRequest
import lab.maxb.dark.data.remote.model.AuthResponse
import lab.maxb.dark.domain.model.AuthState

interface AuthRemoteDataSource {
    suspend fun login(request: AuthRequest): AuthResponse
    suspend fun signup(request: AuthRequest): AuthResponse

    val authState: SharedFlow<AuthState>
}

