package lab.maxb.dark.Presentation.Repository

import android.content.Context
import lab.maxb.dark.Presentation.Room.LocalDatabase.Companion.getDatabase
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import lab.maxb.dark.Presentation.Room.DAO.RecognitionTaskDAO
import lab.maxb.dark.Presentation.Room.Model.RecognitionTaskDTO
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.Presentation.Room.Model.RecognitionTaskName

class RecognitionTasksRepository(applicationContext: Context) : IRecognitionTasksRepository {
    private val mRecognitionTaskDao: RecognitionTaskDAO
    private val recognitionTasks: LiveData<List<RecognitionTask>?>


    init {
        val db = getDatabase(applicationContext)
        mRecognitionTaskDao = db.recognitionTaskDao()
        recognitionTasks = Transformations.map(mRecognitionTaskDao.getAllRecognitionTasks()) {
            data -> data?.map { it.toRecognitionTask() }
        }
    }

    override fun getAllRecognitionTasks(): LiveData<List<RecognitionTask>?>
        = recognitionTasks

    override suspend fun <T : RecognitionTask> addRecognitionTask(task: T) {
        mRecognitionTaskDao.addRecognitionTask(
            RecognitionTaskDTO(task),
            task.names!!.map {
                RecognitionTaskName(task.id, it)
            }
        )
    }

    override suspend fun <T : RecognitionTask> deleteRecognitionTask(task: T) {
        mRecognitionTaskDao.deleteRecognitionTask(
            task as RecognitionTaskDTO
        )
    }
}