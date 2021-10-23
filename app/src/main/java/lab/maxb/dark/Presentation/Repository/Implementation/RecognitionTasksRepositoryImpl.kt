package lab.maxb.dark.Presentation.Repository.Implementation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.Presentation.Repository.Interfaces.RecognitionTasksRepository
import lab.maxb.dark.Presentation.Repository.Room.DAO.RecognitionTaskDAO
import lab.maxb.dark.Presentation.Repository.Room.LocalDatabase.Companion.getDatabase
import lab.maxb.dark.Presentation.Repository.Room.Model.RecognitionTaskDTO
import lab.maxb.dark.Presentation.Repository.Room.Model.RecognitionTaskImage
import lab.maxb.dark.Presentation.Repository.Room.Model.RecognitionTaskName

class RecognitionTasksRepositoryImpl(applicationContext: Context) : RecognitionTasksRepository {
    private val mRecognitionTaskDao: RecognitionTaskDAO
    private val recognitionTasks: LiveData<List<RecognitionTask>?>

    init {
        val db = getDatabase(applicationContext)
        mRecognitionTaskDao = db.recognitionTaskDao()
        recognitionTasks = mRecognitionTaskDao.getAllRecognitionTasks().distinctUntilChanged().map {
            data -> data?.map { it.toRecognitionTask() }
        }
    }

    override fun getAllRecognitionTasks(): LiveData<List<RecognitionTask>?>
        = recognitionTasks

    override fun getRecognitionTask(id: String): LiveData<RecognitionTask?>
        = mRecognitionTaskDao.getRecognitionTask(id).distinctUntilChanged().map {
            it?.toRecognitionTask()
        }

    override suspend fun <T : RecognitionTask> addRecognitionTask(task: T) {
        mRecognitionTaskDao.addRecognitionTask(
            RecognitionTaskDTO(task),
            task.names!!.map {
                RecognitionTaskName(task.id, it)
            },
            task.images!!.map {
                RecognitionTaskImage(task.id, it)
            }
        )
    }

    override suspend fun <T : RecognitionTask> deleteRecognitionTask(task: T) {
        mRecognitionTaskDao.deleteRecognitionTask(
            task as RecognitionTaskDTO
        )
    }
}