package lab.maxb.dark.domain.repository

import androidx.paging.PagingData
import com.bumptech.glide.load.model.GlideUrl
import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.domain.model.RecognitionTaskWithOwner
import lab.maxb.dark.domain.repository.utils.Resource

interface RecognitionTasksRepository {
    val recognitionTaskResource: Resource<String, RecognitionTask>
    fun getAllRecognitionTasks(): Flow<PagingData<RecognitionTaskWithOwner>>
    fun getFavoriteRecognitionTasks(): Flow<PagingData<RecognitionTaskWithOwner>>
    fun hasFavoriteRecognitionTasks(): Flow<Boolean>
    fun getRecognitionTaskImage(path: String): GlideUrl
    suspend fun addRecognitionTask(task: RecognitionTask)
    suspend fun markRecognitionTask(task: RecognitionTask): Boolean
    suspend fun markFavoriteRecognitionTask(task: RecognitionTask)
    suspend fun solveRecognitionTask(id: String, answer: String): Boolean
}