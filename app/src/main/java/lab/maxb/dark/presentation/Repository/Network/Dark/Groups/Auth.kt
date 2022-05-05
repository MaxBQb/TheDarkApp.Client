package lab.maxb.dark.presentation.Repository.Network.Dark.Groups

import lab.maxb.dark.presentation.Repository.Network.Dark.Model.AuthRequest
import lab.maxb.dark.presentation.Repository.Network.Dark.Model.AuthResponse
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