package lab.maxb.dark.Presentation.Repository.Network.Dark.Groups

import lab.maxb.dark.Domain.Model.User
import retrofit2.http.GET
import retrofit2.http.Path

interface User {
    @GET("$path/{id}")
    suspend fun getUser(@Path("id") id: String): User?

    companion object {
        const val path = "/user"
    }
}