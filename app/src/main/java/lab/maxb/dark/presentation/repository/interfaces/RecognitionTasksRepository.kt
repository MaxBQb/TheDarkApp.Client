package lab.maxb.dark.presentation.repository.interfaces

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.domain.model.RecognitionTask

interface RecognitionTasksRepository {
    fun getAllRecognitionTasks(): Flow<PagingData<RecognitionTask>>
    suspend fun getRecognitionTask(id: String, forceUpdate: Boolean = false): Flow<RecognitionTask?>
    suspend fun addRecognitionTask(task: RecognitionTask)
    suspend fun markRecognitionTask(task: RecognitionTask)
    suspend fun deleteRecognitionTask(task: RecognitionTask)
}