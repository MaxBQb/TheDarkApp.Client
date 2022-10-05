package lab.maxb.dark.domain.repository

import androidx.paging.PagingData
import com.bumptech.glide.load.model.GlideUrl
import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.domain.model.RecognitionTask

interface RecognitionTasksRepository {
    fun getAllRecognitionTasks(): Flow<PagingData<RecognitionTask>>
    suspend fun getRecognitionTask(id: String, forceUpdate: Boolean = false): Flow<RecognitionTask?>
    suspend fun refresh(id: String)
    fun getRecognitionTaskImage(path: String): GlideUrl
    suspend fun addRecognitionTask(task: RecognitionTask)
    suspend fun markRecognitionTask(task: RecognitionTask): Boolean
    suspend fun solveRecognitionTask(id: String, answer: String): Boolean
}