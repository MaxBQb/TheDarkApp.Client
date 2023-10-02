package lab.maxb.dark.data.remote.datasource

import lab.maxb.dark.data.remote.model.RecognitionTaskCreationNetworkDTO
import lab.maxb.dark.data.remote.model.RecognitionTaskFullViewNetworkDTO
import lab.maxb.dark.data.remote.model.RecognitionTaskListViewNetworkDTO

interface RecognitionTasksRemoteDataSource {
    suspend fun getAllTasks(page: Int, size: Int): List<RecognitionTaskListViewNetworkDTO>?
    suspend fun getTask(id: String): RecognitionTaskFullViewNetworkDTO?
    suspend fun addTask(task: RecognitionTaskCreationNetworkDTO): RecognitionTaskFullViewNetworkDTO?
    suspend fun markTask(id: String, isAllowed: Boolean): Boolean
    suspend fun solveTask(id: String, answer: String): Boolean
}

