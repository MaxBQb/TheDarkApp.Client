package lab.maxb.dark.Presentation.Repository.Interfaces

import androidx.lifecycle.LiveData
import lab.maxb.dark.Domain.Model.RecognitionTask

interface RecognitionTasksRepository {
    suspend fun getAllRecognitionTasksByReview(isReviewed: Boolean): LiveData<List<RecognitionTask>?>
    suspend fun getAllRecognitionTasks(): LiveData<List<RecognitionTask>?>
    suspend fun getRecognitionTask(id: String): LiveData<RecognitionTask?>
    suspend fun <T : RecognitionTask> addRecognitionTask(task: T)
    suspend fun <T : RecognitionTask> markRecognitionTask(task: T)
    suspend fun <T : RecognitionTask> deleteRecognitionTask(task: T)
}