package lab.maxb.dark.Presentation.Repository.Implementation

import android.content.Context
import lab.maxb.dark.Presentation.Repository.Room.LocalDatabase.Companion.getDatabase
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import lab.maxb.dark.Presentation.Repository.Room.DAO.RecognitionTaskDAO
import lab.maxb.dark.Presentation.Repository.Room.Model.RecognitionTaskDTO
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.Presentation.Repository.Interfaces.IRecognitionTasksRepository
import lab.maxb.dark.Presentation.Repository.Room.Model.RecognitionTaskImage
import lab.maxb.dark.Presentation.Repository.Room.Model.RecognitionTaskName

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

    override fun getRecognitionTask(id: String): LiveData<RecognitionTask?>
        = Transformations.map(mRecognitionTaskDao.getRecognitionTask(id)) {
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