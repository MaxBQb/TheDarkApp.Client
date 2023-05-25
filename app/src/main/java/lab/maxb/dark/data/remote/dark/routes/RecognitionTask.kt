package lab.maxb.dark.data.remote.dark.routes

import lab.maxb.dark.data.model.remote.RecognitionTaskCreationNetworkDTO
import lab.maxb.dark.data.model.remote.RecognitionTaskFullViewNetworkDTO
import lab.maxb.dark.data.model.remote.RecognitionTaskListViewNetworkDTO
import retrofit2.http.*

interface RecognitionTask {
    @GET("$path/")
    suspend fun getAllTasks(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 2,
    ): List<RecognitionTaskListViewNetworkDTO>?

    @GET("$path/{id}")
    suspend fun getTask(@Path("id") id: String): RecognitionTaskFullViewNetworkDTO?

    @POST("$path/")
    suspend fun addTask(@Body task: RecognitionTaskCreationNetworkDTO): RecognitionTaskFullViewNetworkDTO?

    @POST("$path/{id}/approve/")
    suspend fun approveTask(@Path("id") id: String)

    @POST("$path/{id}/decline/")
    suspend fun declineTask(@Path("id") id: String)

    @POST("$path/{id}/solutions/")
    suspend fun solveTask(
        @Path("id") id: String,
        @Query("answer") answer: String
    ): Boolean

    companion object {
        const val path = "/tasks"
    }
}
