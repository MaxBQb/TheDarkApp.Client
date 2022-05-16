package lab.maxb.dark.presentation.repository.interfaces

import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.domain.model.RecognitionTask

interface RecognitionTasksRepository {
    suspend fun getAllRecognitionTasks(): Flow<List<RecognitionTask>?>
    suspend fun getRecognitionTask(id: String, forceUpdate: Boolean = false): Flow<RecognitionTask?>
    suspend fun <T : RecognitionTask> addRecognitionTask(task: T)
    suspend fun <T : RecognitionTask> markRecognitionTask(task: T)
    suspend fun <T : RecognitionTask> deleteRecognitionTask(task: T)
    suspend fun clearCache()
}