package lab.maxb.dark.presentation.repository.network.dark.routes

import lab.maxb.dark.presentation.repository.network.dark.model.AuthRequest
import lab.maxb.dark.presentation.repository.network.dark.model.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST


interface Auth {
    @POST("$path/login")
    suspend fun login(@Body request: AuthRequest): AuthResponse

    @POST("$path/signup")
    suspend fun signup(@Body request: AuthRequest): AuthResponse

    companion object {
        const val path = "/auth"
    }
}