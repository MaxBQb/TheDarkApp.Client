package lab.maxb.dark.data.datasource.remote

import lab.maxb.dark.data.remote.dark.routes.UsersAPI
import org.koin.core.annotation.Singleton

@Singleton
class UsersRetrofitDataSource(
    private val api: UsersAPI,
) : UsersRemoteDataSource by api