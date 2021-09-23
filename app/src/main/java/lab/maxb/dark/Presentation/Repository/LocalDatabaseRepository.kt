package lab.maxb.dark.Presentation.Repository

import lab.maxb.dark.Presentation.Room.LocalDatabase.Companion.getDatabase
import android.app.Application
import androidx.lifecycle.LiveData
import lab.maxb.dark.Presentation.Room.DAO.RecognitionTaskDAO
import lab.maxb.dark.Presentation.Room.Model.RecognitionTaskDTO
import lab.maxb.dark.Domain.Model.RecognitionTask

class LocalDatabaseRepository(application: Application?) : RepositoryTasks {
    private val mRecognitionTaskDao: RecognitionTaskDAO
    private val recognitionTasks: LiveData<List<RecognitionTask>?>

    init {
        val db = getDatabase(application!!)
        mRecognitionTaskDao = db.recognitionTaskDao()
        recognitionTasks = mRecognitionTaskDao.getAllRecognitionTasks() as LiveData<List<RecognitionTask>?>
    }

    override fun getAllRecognitionTasks(): LiveData<List<RecognitionTask>?>
        = recognitionTasks

    override suspend fun <T : RecognitionTask> addRecognitionTask(task: T) {
        mRecognitionTaskDao.addRecognitionTask(
            RecognitionTaskDTO(task)
        )
    }

    override suspend fun <T : RecognitionTask> deleteRecognitionTask(task: T) {
        mRecognitionTaskDao.deleteRecognitionTask(
            task as RecognitionTaskDTO?
        )
    }
}