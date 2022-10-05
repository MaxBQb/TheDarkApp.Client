package lab.maxb.dark.data.remote.dark.routes

import lab.maxb.dark.data.model.remote.AuthRequest
import lab.maxb.dark.data.model.remote.AuthResponse
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