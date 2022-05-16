package lab.maxb.dark.presentation.repository.implementation

import androidx.core.net.toUri
import androidx.room.withTransaction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapLatest
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.presentation.extra.ImageLoader
import lab.maxb.dark.presentation.repository.interfaces.ImagesRepository
import lab.maxb.dark.presentation.repository.interfaces.RecognitionTasksRepository
import lab.maxb.dark.presentation.repository.interfaces.UsersRepository
import lab.maxb.dark.presentation.repository.network.dark.DarkService
import lab.maxb.dark.presentation.repository.room.LocalDatabase
import lab.maxb.dark.presentation.repository.room.dao.RecognitionTaskDAO
import lab.maxb.dark.presentation.repository.room.model.RecognitionTaskDTO
import lab.maxb.dark.presentation.repository.room.model.RecognitionTaskImageCrossref
import lab.maxb.dark.presentation.repository.room.model.RecognitionTaskName
import lab.maxb.dark.presentation.repository.utils.Resource
import lab.maxb.dark_api.Model.POJO.RecognitionTaskCreationDTO
import org.koin.core.annotation.Single

@Single
class RecognitionTasksRepositoryImpl(
    private val db: LocalDatabase,
    private val mDarkService: DarkService,
    private val usersRepository: UsersRepository,
    private val imagesRepository: ImagesRepository,
    private val imageLoader: ImageLoader,
) : RecognitionTasksRepository {
    private val mRecognitionTaskDao: RecognitionTaskDAO = db.recognitionTaskDao()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val tasksResource = Resource<Unit, List<RecognitionTask>>().apply {
        fetchLocal = {
            mRecognitionTaskDao.getAllRecognitionTasks().mapLatest {
                data -> data?.map { it.toRecognitionTask() }
            }
        }
        fetchRemote = {
            mDarkService.getAllTasks()?.map {
                RecognitionTask(
                    setOf(),
                    listOf(imagesRepository.getById(it.image!!).firstOrNull()!!),
                    usersRepository.getUser(it.owner_id).firstOrNull()!!
                )
            }
        }
        localStore = {
            db.withTransaction {
                it.forEach { task ->
                    mRecognitionTaskDao.addRecognitionTask(
                        RecognitionTaskDTO(task)
                    )
                    mRecognitionTaskDao.addRecognitionTaskImages(
                        listOf(RecognitionTaskImageCrossref(
                            task.id,
                            task.images!![0].id,
                        ))
                    )
                }
            }
        }
        clearLocalStore = {
            mRecognitionTaskDao.clear()
        }
    }

    override suspend fun getAllRecognitionTasks():
            Flow<List<RecognitionTask>?>
        = tasksResource.query(Unit)

    override suspend fun <T : RecognitionTask> addRecognitionTask(task: T) {
        val taskLocal = RecognitionTaskDTO(task)
        mDarkService.addTask(RecognitionTaskCreationDTO(
            task.names!!
        ))?.also { taskLocal.id = it }

        val images = task.images!!.map {
            it.id = mDarkService.addImage(
                taskLocal.id,
                imageLoader.fromUri(it.path.toUri())
            )!!
            imagesRepository.save(it)
            RecognitionTaskImageCrossref(
                taskLocal.id, it.id,
            )
        }

        mRecognitionTaskDao.addRecognitionTask(
            taskLocal,
            task.names!!.map {
                RecognitionTaskName(taskLocal.id, it)
            },
            images
        )
    }

    override suspend fun <T : RecognitionTask> markRecognitionTask(task: T) {
        mRecognitionTaskDao.updateRecognitionTask(task as RecognitionTaskDTO)
        try {
            if (mDarkService.markTask(task.id, task.reviewed))
                getRecognitionTask(task.id, true).firstOrNull()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override suspend fun <T : RecognitionTask> deleteRecognitionTask(task: T) {
        mRecognitionTaskDao.deleteRecognitionTask(
            task as RecognitionTaskDTO
        )
    }

    override suspend fun clearCache(): Unit
        = mRecognitionTaskDao.clear()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val taskResource = Resource<String, RecognitionTask>().apply {
        fetchRemote = { id ->
            mDarkService.getTask(id)?.let { task ->
                RecognitionTask(
                    task.names,
                    task.images?.map { image ->
                        imagesRepository.getById(image).firstOrNull()!!
                    },
                    usersRepository.getUser(
                        task.owner_id
                    ).firstOrNull()!!,
                )
            }
        }
        fetchLocal = { id ->
            mRecognitionTaskDao.getRecognitionTask(id).mapLatest {
                it?.toRecognitionTask()
            }
        }
        localStore = { task ->
            mRecognitionTaskDao.addRecognitionTask(
                RecognitionTaskDTO(task),
                task.names!!.map { name ->
                    RecognitionTaskName(task.id, name)
                },
                task.images!!.map { image ->
                    RecognitionTaskImageCrossref(
                        task.id,
                        image.id,
                    )
                },
            )
        }
        clearLocalStore = {
            mRecognitionTaskDao.deleteRecognitionTask(it)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getRecognitionTask(id: String, forceUpdate: Boolean)
        = taskResource.query(id, forceUpdate)
}