package lab.maxb.dark.data.datasource.remote

import lab.maxb.dark.data.BuildConfig
import lab.maxb.dark.data.remote.dark.routes.ImagesAPI
import org.koin.core.annotation.Single

@Single
class ImagesRetrofitDataSource(
    private val api: ImagesAPI,
) : ImagesRemoteDataSource by api {
    override fun getImageSource(path: String)
        = "${BuildConfig.DARK_API_URL}${ImagesAPI.path}/$path"
}