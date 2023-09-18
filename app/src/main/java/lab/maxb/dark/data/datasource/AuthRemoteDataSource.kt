package lab.maxb.dark.data.datasource

import kotlinx.coroutines.flow.SharedFlow
import lab.maxb.dark.data.model.remote.AuthRequest
import lab.maxb.dark.data.model.remote.AuthResponse
import lab.maxb.dark.domain.model.AuthState

interface AuthRemoteDataSource {
    suspend fun login(request: AuthRequest): AuthResponse
    suspend fun signup(request: AuthRequest): AuthResponse

    val authState: SharedFlow<AuthState>
}

