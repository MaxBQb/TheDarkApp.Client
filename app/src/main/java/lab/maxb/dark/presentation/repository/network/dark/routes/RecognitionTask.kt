package lab.maxb.dark.presentation.repository.network.dark.routes

import lab.maxb.dark.presentation.repository.network.dark.model.RecognitionTaskCreationDTO
import lab.maxb.dark.presentation.repository.network.dark.model.RecognitionTaskFullViewDTO
import lab.maxb.dark.presentation.repository.network.dark.model.RecognitionTaskListViewDTO
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface RecognitionTask {
    @GET("$path/all")
    suspend fun getAllTasks(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 2,
    ): List<RecognitionTaskListViewDTO>?

    @GET("$path/{id}")
    suspend fun getTask(@Path("id") id: String): RecognitionTaskFullViewDTO?

    @POST("$path/add")
    suspend fun addTask(@Body task: RecognitionTaskCreationDTO): String?

    @PATCH("$path/mark/{id}/{isAllowed}")
    suspend fun markTask(
        @Path("id") id: String,
        @Path("isAllowed") isAllowed: Boolean
    ): Boolean

    @Multipart
    @POST("$path/{id}/image")
    suspend fun addImage(
        @Path("id") id: String,
        @Part filePart: MultipartBody.Part
    ): String?

    @Streaming
    @GET("$path/image/{path}")
    suspend fun downloadImage(
        @Path("path") path: String
    ): ResponseBody?

    companion object {
        const val path = "/task"
    }
}
