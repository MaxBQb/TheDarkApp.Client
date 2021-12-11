package lab.maxb.dark.Presentation.Repository.Network.Dark.Groups

import lab.maxb.dark.Presentation.Repository.Network.Dark.Model.AuthRequest
import lab.maxb.dark.Presentation.Repository.Network.Dark.Model.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST


interface Auth {
    @POST("$path/login")
    suspend fun login(@Body request: AuthRequest): AuthResponse

    companion object {
        const val path = "/auth"
    }
}