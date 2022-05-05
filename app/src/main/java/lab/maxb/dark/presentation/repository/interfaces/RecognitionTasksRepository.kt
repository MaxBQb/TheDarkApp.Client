package lab.maxb.dark.presentation.repository.interfaces

import androidx.lifecycle.LiveData
import lab.maxb.dark.domain.model.RecognitionTask

interface RecognitionTasksRepository {
    fun getAllRecognitionTasksByReview(isReviewed: Boolean): LiveData<List<RecognitionTask>?>
    fun getAllRecognitionTasks(): LiveData<List<RecognitionTask>?>
    fun getRecognitionTask(id: String): LiveData<RecognitionTask?>
    suspend fun <T : RecognitionTask> addRecognitionTask(task: T)
    suspend fun <T : RecognitionTask> markRecognitionTask(task: T)
    suspend fun <T : RecognitionTask> deleteRecognitionTask(task: T)
    suspend fun clearCache()
}