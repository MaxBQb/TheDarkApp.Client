package lab.maxb.dark.data.remote.route

import lab.maxb.dark.data.remote.datasource.UsersRemoteDataSource
import lab.maxb.dark.domain.model.User
import retrofit2.http.GET
import retrofit2.http.Path

interface UsersAPI : UsersRemoteDataSource {
    @GET("$path/{id}")
    override suspend fun getUser(@Path("id") id: String): User?

    companion object {
        const val path = "/users"
    }
}