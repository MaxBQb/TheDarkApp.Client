package lab.maxb.dark.Presentation.Repository.Network.Dark.Groups

import lab.maxb.dark_api.Model.POJO.RecognitionTaskListViewDTO
import retrofit2.http.GET

interface RecognitionTask {
    @GET("$path/all")
    suspend fun getAllTasks(): List<RecognitionTaskListViewDTO>?

    companion object {
        const val path = "/task"
    }
}