package lab.maxb.dark.data.remote.route

import lab.maxb.dark.data.remote.datasource.AuthRemoteDataSource
import lab.maxb.dark.data.remote.model.AuthRequest
import lab.maxb.dark.data.remote.model.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthAPI : AuthRemoteDataSource {
    @POST("$path/login")
    override suspend fun login(@Body request: AuthRequest): AuthResponse

    @POST("$path/signup")
    override suspend fun signup(@Body request: AuthRequest): AuthResponse

    companion object {
        const val path = "/auth"
    }
}