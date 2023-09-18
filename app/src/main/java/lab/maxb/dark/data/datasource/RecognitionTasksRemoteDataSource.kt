package lab.maxb.dark.data.datasource

import lab.maxb.dark.data.model.remote.RecognitionTaskCreationNetworkDTO
import lab.maxb.dark.data.model.remote.RecognitionTaskFullViewNetworkDTO
import lab.maxb.dark.data.model.remote.RecognitionTaskListViewNetworkDTO

interface RecognitionTasksRemoteDataSource {
    suspend fun getAllTasks(page: Int, size: Int): List<RecognitionTaskListViewNetworkDTO>?
    suspend fun getTask(id: String): RecognitionTaskFullViewNetworkDTO?
    suspend fun addTask(task: RecognitionTaskCreationNetworkDTO): RecognitionTaskFullViewNetworkDTO?
    suspend fun markTask(id: String, isAllowed: Boolean): Boolean
    suspend fun solveTask(id: String, answer: String): Boolean
}

