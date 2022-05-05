package lab.maxb.dark.presentation.Repository.Network.Dark.Groups

import lab.maxb.dark_api.Model.POJO.RecognitionTaskCreationDTO
import lab.maxb.dark_api.Model.POJO.RecognitionTaskFullViewDTO
import lab.maxb.dark_api.Model.POJO.RecognitionTaskListViewDTO
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface RecognitionTask {
    @GET("$path/all")
    suspend fun getAllTasks(): List<RecognitionTaskListViewDTO>?

    @GET("$path/{id}")
    suspend fun getTask(@Path("id") id: String): RecognitionTaskFullViewDTO?

    @POST("$path/add")
    suspend fun addTask(@Body task: RecognitionTaskCreationDTO): String?

    @PATCH("$path/mark/{id}")
    suspend fun markTask(@Path("id") id: String,
                         @Query("isAllowed") isAllowed: Boolean):
            Boolean

    @Multipart
    @POST("$path/{id}/image")
    suspend fun addImage(
        @Path("id") id: String,
        @Part filePart: MultipartBody.Part
    ): String?

    @Streaming
    @GET("$path/image/{path}")
    suspend fun downloadImage(@Path("path") path: String): ResponseBody?

    companion object {
        const val path = "/task"
    }
}
