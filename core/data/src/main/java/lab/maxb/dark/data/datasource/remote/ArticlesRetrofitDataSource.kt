package lab.maxb.dark.data.datasource.remote

import lab.maxb.dark.data.remote.dark.routes.ArticlesAPI
import org.koin.core.annotation.Single

@Single
class ArticlesRetrofitDataSource(
    private val api: ArticlesAPI,
) : ArticlesRemoteDataSource by api