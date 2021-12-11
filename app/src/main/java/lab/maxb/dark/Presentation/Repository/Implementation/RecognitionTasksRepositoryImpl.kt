package lab.maxb.dark.Presentation.Repository.Implementation

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.Presentation.Repository.Interfaces.RecognitionTasksRepository
import lab.maxb.dark.Presentation.Repository.Network.Dark.DarkService
import lab.maxb.dark.Presentation.Repository.Room.DAO.RecognitionTaskDAO
import lab.maxb.dark.Presentation.Repository.Room.LocalDatabase
import lab.maxb.dark.Presentation.Repository.Room.Model.RecognitionTaskDTO
import lab.maxb.dark.Presentation.Repository.Room.Model.RecognitionTaskImage
import lab.maxb.dark.Presentation.Repository.Room.Model.RecognitionTaskName

class RecognitionTasksRepositoryImpl(
    db: LocalDatabase,
    private val mDarkService: DarkService
) : RecognitionTasksRepository {
    private val mRecognitionTaskDao: RecognitionTaskDAO = db.recognitionTaskDao()

    override fun getAllRecognitionTasksByReview(isReviewed: Boolean): LiveData<List<RecognitionTask>?>
        = mRecognitionTaskDao.getAllRecognitionTasksByReview(isReviewed)
        .distinctUntilChanged().map {
            data -> data?.map { it.toRecognitionTask() }
        }

    override suspend fun getAllRecognitionTasks(): LiveData<List<RecognitionTask>?>
        = mRecognitionTaskDao
        .getAllRecognitionTasks()
        .distinctUntilChanged().map {
            data -> data?.map { it.toRecognitionTask() }
        }.also { try {
            mDarkService.getAllTasks()?.forEach { task ->
            //    addRecognitionTask(task)

            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }}

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

    override suspend fun <T : RecognitionTask> updateRecognitionTask(task: T) {
        mRecognitionTaskDao.updateRecognitionTask(
            task as RecognitionTaskDTO
        )
    }

    override suspend fun <T : RecognitionTask> deleteRecognitionTask(task: T) {
        mRecognitionTaskDao.deleteRecognitionTask(
            task as RecognitionTaskDTO
        )
    }
}