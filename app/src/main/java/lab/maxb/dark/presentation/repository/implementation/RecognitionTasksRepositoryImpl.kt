package lab.maxb.dark.presentation.repository.implementation

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.flow.first
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.presentation.extra.ImageLoader
import lab.maxb.dark.presentation.repository.interfaces.RecognitionTasksRepository
import lab.maxb.dark.presentation.repository.interfaces.UsersRepository
import lab.maxb.dark.presentation.repository.network.dark.DarkService
import lab.maxb.dark.presentation.repository.room.dao.RecognitionTaskDAO
import lab.maxb.dark.presentation.repository.room.LocalDatabase
import lab.maxb.dark.presentation.repository.room.model.RecognitionTaskDTO
import lab.maxb.dark.presentation.repository.room.model.RecognitionTaskImage
import lab.maxb.dark.presentation.repository.room.model.RecognitionTaskName
import lab.maxb.dark_api.Model.POJO.RecognitionTaskCreationDTO
import org.koin.core.annotation.Single

@Single
class RecognitionTasksRepositoryImpl(
    db: LocalDatabase,
    private val mDarkService: DarkService,
    private val usersRepository: UsersRepository,
    private val imageLoader: ImageLoader,
) : RecognitionTasksRepository {
    private val mRecognitionTaskDao: RecognitionTaskDAO = db.recognitionTaskDao()

    override fun getAllRecognitionTasksByReview(isReviewed: Boolean):
            LiveData<List<RecognitionTask>?> = liveData {
        emitSource(
            mRecognitionTaskDao.getAllRecognitionTasksByReview(isReviewed)
            .distinctUntilChanged().map {
                data -> data?.map { it.toRecognitionTask() }
            }
        )
        refreshRecognitionTaskList()
    }

    override fun getAllRecognitionTasks():
            LiveData<List<RecognitionTask>?> = liveData {
        emitSource(
            mRecognitionTaskDao.getAllRecognitionTasks()
            .distinctUntilChanged().map {
                data -> data?.map { it.toRecognitionTask() }
            }
        )
        refreshRecognitionTaskList()
    }

    private suspend fun refreshRecognitionTaskList() = try {
        mDarkService.getAllTasks()?.also { tasks ->
            mRecognitionTaskDao.deleteRecognitionTasks(
                tasks.map { it.id }
            )
        }?.forEach { task ->
            mRecognitionTaskDao.addRecognitionTask(
                RecognitionTaskDTO(
                    task.id,
                    usersRepository.getUser(
                        task.owner_id
                    ).first()!!.id,
                    task.reviewed
                )
            )
            if (mRecognitionTaskDao.getRecognitionTaskImages(task.id)
                    .none { it.id == task.image!! })
                mRecognitionTaskDao.addRecognitionTaskImages(
                    listOf(RecognitionTaskImage(task.id,
                        refreshImage(task.image!!)
                    ))
                )
        }
    } catch (e: Throwable) {
        e.printStackTrace()
    }

    private suspend fun refreshRecognitionTask(id: String) = try {
        mDarkService.getTask(id)?.let { task ->
            val images = mRecognitionTaskDao.getRecognitionTaskImages(task.id)
            mRecognitionTaskDao.addRecognitionTask(
                RecognitionTaskDTO(
                    task.id,
                    usersRepository.getUser(
                        task.owner_id
                    ).first()!!.id,
                    task.reviewed
                ),
                task.names!!.map { RecognitionTaskName(
                    task.id, it
                ) },
                task.images!!.map { image ->
                    val cache = images.find { it.id == image }
                    RecognitionTaskImage(
                        task.id,
                        cache?.image ?: refreshImage(image)
                    )
                }
            )
        }
    } catch (e: Throwable) {
        e.printStackTrace()
    }

    override fun getRecognitionTask(id: String):
            LiveData<RecognitionTask?> = liveData {
        if (mRecognitionTaskDao.hasRecognitionTask(id)) {
            emitSource(
                mRecognitionTaskDao.getRecognitionTask(id)
                    .distinctUntilChanged().map {
                    it?.toRecognitionTask()
                }
            )
            refreshRecognitionTask(id)
        } else {
            refreshRecognitionTask(id)
            emitSource(
                mRecognitionTaskDao.getRecognitionTask(id)
                    .distinctUntilChanged().map {
                        it?.toRecognitionTask()
                    }
            )
        }
    }

    override suspend fun <T : RecognitionTask> addRecognitionTask(task: T) {
        val taskLocal = RecognitionTaskDTO(task)
        mDarkService.addTask(RecognitionTaskCreationDTO(
            task.names!!
        ))?.also { taskLocal.id = it }

        val images = task.images?.map { RecognitionTaskImage(
            taskLocal.id, it,
            mDarkService.addImage(
                taskLocal.id,
                imageLoader.fromUri(it.toUri())
            )!!
        ) }

        mRecognitionTaskDao.addRecognitionTask(
            taskLocal,
            task.names!!.map {
                RecognitionTaskName(taskLocal.id, it)
            },
            images!!
        )
    }

    override suspend fun <T : RecognitionTask> markRecognitionTask(task: T) {
        mRecognitionTaskDao.updateRecognitionTask(task as RecognitionTaskDTO)
        try {
            if (mDarkService.markTask(task.id, task.reviewed))
                refreshRecognitionTaskList()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override suspend fun <T : RecognitionTask> deleteRecognitionTask(task: T) {
        mRecognitionTaskDao.deleteRecognitionTask(
            task as RecognitionTaskDTO
        )
    }

    private suspend fun refreshImage(id: String) = try {
        imageLoader.fromResponse(
            mDarkService.downloadImage(id),
            id
        )
    } catch (e: NullPointerException) {
        println("Image not found on server: $id")
        id
    } catch (e: Throwable) {
        e.printStackTrace()
        id
    }

    override suspend fun clearCache(): Unit
        = mRecognitionTaskDao.clear()
}