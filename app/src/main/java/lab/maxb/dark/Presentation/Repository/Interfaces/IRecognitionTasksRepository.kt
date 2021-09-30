package lab.maxb.dark.Presentation.Repository.Interfaces

import androidx.lifecycle.LiveData
import lab.maxb.dark.Domain.Model.RecognitionTask

interface IRecognitionTasksRepository {
    fun getAllRecognitionTasks(): LiveData<List<RecognitionTask>?>
    fun getRecognitionTask(id: String): LiveData<RecognitionTask?>
    suspend fun <T : RecognitionTask> addRecognitionTask(task: T)
    suspend fun <T : RecognitionTask> deleteRecognitionTask(task: T)
}