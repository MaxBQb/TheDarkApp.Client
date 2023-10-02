package lab.maxb.dark.data.remote.datasource

import lab.maxb.dark.data.remote.route.UsersAPI
import org.koin.core.annotation.Singleton

@Singleton
class UsersRetrofitDataSource(
    private val api: UsersAPI,
) : UsersRemoteDataSource by api