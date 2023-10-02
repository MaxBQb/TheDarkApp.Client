package lab.maxb.dark.data.remote.datasource

import lab.maxb.dark.data.remote.route.RecognitionTasksAPI
import org.koin.core.annotation.Single

@Single
class RecognitionTasksRetrofitDataSource(
    private val api: RecognitionTasksAPI,
): RecognitionTasksRemoteDataSource by api {
    override suspend fun markTask(id: String, isAllowed: Boolean) = try {
        if (isAllowed)
            api.approveTask(id)
        else
            api.declineTask(id)
        true
    } catch (e: Exception) { false }
}