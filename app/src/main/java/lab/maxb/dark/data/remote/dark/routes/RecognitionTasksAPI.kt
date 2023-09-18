package lab.maxb.dark.data.remote.dark.routes

import lab.maxb.dark.data.datasource.remote.RecognitionTasksRemoteDataSource
import lab.maxb.dark.data.model.remote.RecognitionTaskCreationNetworkDTO
import lab.maxb.dark.data.model.remote.RecognitionTaskFullViewNetworkDTO
import lab.maxb.dark.data.model.remote.RecognitionTaskListViewNetworkDTO
import retrofit2.http.*

interface RecognitionTasksAPI : RecognitionTasksRemoteDataSource {
    @GET("$path/")
    override suspend fun getAllTasks(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): List<RecognitionTaskListViewNetworkDTO>?

    @GET("$path/{id}")
    override suspend fun getTask(@Path("id") id: String): RecognitionTaskFullViewNetworkDTO?

    @POST("$path/")
    override suspend fun addTask(@Body task: RecognitionTaskCreationNetworkDTO): RecognitionTaskFullViewNetworkDTO?

    @POST("$path/{id}/approve/")
    suspend fun approveTask(@Path("id") id: String)

    @POST("$path/{id}/decline/")
    suspend fun declineTask(@Path("id") id: String)

    @POST("$path/{id}/solutions/")
    override suspend fun solveTask(
        @Path("id") id: String,
        @Query("answer") answer: String
    ): Boolean

    companion object {
        const val path = "/tasks"
    }
}
