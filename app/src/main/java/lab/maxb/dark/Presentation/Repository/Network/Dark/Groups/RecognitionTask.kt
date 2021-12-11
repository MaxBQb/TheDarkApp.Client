package lab.maxb.dark.Presentation.Repository.Network.Dark.Groups

import lab.maxb.dark_api.Model.POJO.RecognitionTaskFullViewDTO
import lab.maxb.dark_api.Model.POJO.RecognitionTaskListViewDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecognitionTask {
    @GET("$path/all")
    suspend fun getAllTasks(): List<RecognitionTaskListViewDTO>?

    @GET("${path}/{id}")
    suspend fun getTask(@Path("id") id: String): RecognitionTaskFullViewDTO?

    @GET("${path}/mark/{id}")
    suspend fun markTask(@Path("id") id: String,
                         @Query("isAllowed") isAllowed: Boolean):
            Boolean

    companion object {
        const val path = "/task"
    }
}