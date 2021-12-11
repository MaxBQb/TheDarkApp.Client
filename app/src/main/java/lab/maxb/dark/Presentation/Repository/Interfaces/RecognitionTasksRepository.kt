package lab.maxb.dark.Presentation.Repository.Interfaces

import androidx.lifecycle.LiveData
import lab.maxb.dark.Domain.Model.RecognitionTask

interface RecognitionTasksRepository {
    fun getAllRecognitionTasksByReview(isReviewed: Boolean): LiveData<List<RecognitionTask>?>
    suspend fun getAllRecognitionTasks(): LiveData<List<RecognitionTask>?>
    fun getRecognitionTask(id: String): LiveData<RecognitionTask?>
    suspend fun <T : RecognitionTask> addRecognitionTask(task: T)
    suspend fun <T : RecognitionTask> updateRecognitionTask(task: T)
    suspend fun <T : RecognitionTask> deleteRecognitionTask(task: T)
}