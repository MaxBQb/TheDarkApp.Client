package lab.maxb.dark.Presentation.Repository

import androidx.lifecycle.LiveData
import lab.maxb.dark.Domain.Model.RecognitionTask

interface RepositoryTasks {
    fun getAllRecognitionTasks(): LiveData<List<RecognitionTask>?>
    fun <T : RecognitionTask> addRecognitionTask(task: T)
    fun <T : RecognitionTask> deleteRecognitionTask(task: T)
}