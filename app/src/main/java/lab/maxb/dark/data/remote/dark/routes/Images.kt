package lab.maxb.dark.data.remote.dark.routes

import lab.maxb.dark.BuildConfig
import okhttp3.MultipartBody
import retrofit2.http.*

interface Images {
    @Multipart
    @POST("$path/")
    suspend fun addImage(
        @Part filePart: MultipartBody.Part
    ): String?

    companion object {
        const val path = "/images"
    }
}

@Suppress("UnusedReceiverParameter")
fun Images.getImage(id: String) = "${BuildConfig.DARK_API_URL}${Images.path}/$id"