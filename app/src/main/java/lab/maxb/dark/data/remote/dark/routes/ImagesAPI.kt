package lab.maxb.dark.data.remote.dark.routes

import lab.maxb.dark.data.datasource.ImagesRemoteDataSource
import okhttp3.MultipartBody
import retrofit2.http.*

interface ImagesAPI : ImagesRemoteDataSource {
    @Multipart
    @POST("$path/")
    override suspend fun addImage(
        @Part filePart: MultipartBody.Part
    ): String?

    companion object {
        const val path = "/images"
    }
}
