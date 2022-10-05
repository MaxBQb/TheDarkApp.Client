package lab.maxb.dark.data.remote.dark.routes

import lab.maxb.dark.domain.model.User
import retrofit2.http.GET
import retrofit2.http.Path

interface User {
    @GET("$path/{id}")
    suspend fun getUser(@Path("id") id: String): User?

    companion object {
        const val path = "/user"
    }
}