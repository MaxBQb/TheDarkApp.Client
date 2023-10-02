package lab.maxb.dark.data.remote.datasource

import lab.maxb.dark.data.core.BuildConfig
import lab.maxb.dark.data.remote.route.ImagesAPI
import org.koin.core.annotation.Single

@Single
class ImagesRetrofitDataSource(
    private val api: ImagesAPI,
) : ImagesRemoteDataSource by api {
    override fun getImageSource(path: String)
        = "${BuildConfig.DARK_API_URL}${ImagesAPI.path}/$path"
}