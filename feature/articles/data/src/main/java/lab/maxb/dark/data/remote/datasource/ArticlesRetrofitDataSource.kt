package lab.maxb.dark.data.remote.datasource

import lab.maxb.dark.data.remote.route.ArticlesAPI
import org.koin.core.annotation.Single

@Single
class ArticlesRetrofitDataSource(
    private val api: ArticlesAPI,
) : ArticlesRemoteDataSource by api