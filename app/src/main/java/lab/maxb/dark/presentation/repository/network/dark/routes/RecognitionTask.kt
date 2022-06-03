package lab.maxb.dark.presentation.repository.network.dark.routes

import lab.maxb.dark.presentation.repository.network.dark.model.RecognitionTaskCreationNetworkDTO
import lab.maxb.dark.presentation.repository.network.dark.model.RecognitionTaskFullViewNetworkDTO
import lab.maxb.dark.presentation.repository.network.dark.model.RecognitionTaskListViewNetworkDTO
import okhttp3.MultipartBody
import retrofit2.http.*

interface RecognitionTask {
    @GET("$path/all")
    suspend fun getAllTasks(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 2,
    ): List<RecognitionTaskListViewNetworkDTO>?

    @GET("$path/{id}")
    suspend fun getTask(@Path("id") id: String): RecognitionTaskFullViewNetworkDTO?

    @POST("$path/add")
    suspend fun addTask(@Body task: RecognitionTaskCreationNetworkDTO): String?

    @PATCH("$path/mark/{id}/{isAllowed}")
    suspend fun markTask(
        @Path("id") id: String,
        @Path("isAllowed") isAllowed: Boolean
    ): Boolean

    @GET("$path/solve/{id}")
    suspend fun solveTask(
        @Path("id") id: String,
        @Query("answer") answer: String
    ): Boolean

    @Multipart
    @POST("$path/{id}/image")
    suspend fun addImage(
        @Path("id") id: String,
        @Part filePart: MultipartBody.Part
    ): String?

    companion object {
        const val path = "/task"
    }
}
