package lab.maxb.dark.data.remote.datasource

import lab.maxb.dark.domain.model.User

interface UsersRemoteDataSource {
    suspend fun getUser(id: String): User?
}
