package lab.maxb.dark.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.domain.repository.utils.Resource

interface RecognitionTasksRepository {
    val recognitionTaskResource: Resource<String, RecognitionTask>
    fun getAllRecognitionTasks(): Flow<PagingData<RecognitionTask>>
    fun getFavoriteRecognitionTasks(): Flow<PagingData<RecognitionTask>>
    fun hasFavoriteRecognitionTasks(): Flow<Boolean>
    fun getRecognitionTaskImage(path: String): String
    suspend fun addRecognitionTask(task: RecognitionTask)
    suspend fun markRecognitionTask(task: RecognitionTask): Boolean
    suspend fun markFavoriteRecognitionTask(task: RecognitionTask)
    suspend fun solveRecognitionTask(id: String, answer: String): Boolean
}