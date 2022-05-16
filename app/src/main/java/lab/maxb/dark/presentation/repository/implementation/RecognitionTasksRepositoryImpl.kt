package lab.maxb.dark.presentation.repository.implementation

import androidx.core.net.toUri
import androidx.room.withTransaction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapLatest
import lab.maxb.dark.domain.model.Image
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.presentation.extra.ImageLoader
import lab.maxb.dark.presentation.repository.interfaces.RecognitionTasksRepository
import lab.maxb.dark.presentation.repository.interfaces.UsersRepository
import lab.maxb.dark.presentation.repository.network.dark.DarkService
import lab.maxb.dark.presentation.repository.room.LocalDatabase
import lab.maxb.dark.presentation.repository.room.dao.RecognitionTaskDAO
import lab.maxb.dark.presentation.repository.room.model.RecognitionTaskDTO
import lab.maxb.dark.presentation.repository.room.model.RecognitionTaskImage
import lab.maxb.dark.presentation.repository.room.model.RecognitionTaskName
import lab.maxb.dark.presentation.repository.room.model.toImage
import lab.maxb.dark.presentation.repository.utils.Resource
import lab.maxb.dark.presentation.repository.utils.StaticResource
import lab.maxb.dark_api.Model.POJO.RecognitionTaskCreationDTO
import org.koin.core.annotation.Single

@Single
class RecognitionTasksRepositoryImpl(
    private val db: LocalDatabase,
    private val mDarkService: DarkService,
    private val usersRepository: UsersRepository,
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
                    listOf(getImage(it.image!!)!!),
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
                        listOf(RecognitionTaskImage(
                            task.id,
                            task.images!![0].path,
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

        val images = task.images?.map { RecognitionTaskImage(
            taskLocal.id, it.path,
            mDarkService.addImage(
                taskLocal.id,
                imageLoader.fromUri(it.path.toUri())
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
    private val imageResource = StaticResource<String, Image>().apply {
        fetchRemote = {
            Image(imageLoader.fromResponse(
                mDarkService.downloadImage(it),
                it
            ), it)
        }
        clearLocalStore = {
            mRecognitionTaskDao.deleteRecognitionTaskImage(it)
        }
        fetchLocal = {
            mRecognitionTaskDao.getRecognitionTaskImage(it).mapLatest { image ->
                image?.toImage()
            }
        }
    }

    private suspend fun getImage(id: String) = imageResource.query(id).firstOrNull()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val taskResource = Resource<String, RecognitionTask>().apply {
        fetchRemote = { id ->
            mDarkService.getTask(id)?.let { task ->
                RecognitionTask(
                    task.names,
                    task.images?.map { image ->
                        getImage(image)!!
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
                    RecognitionTaskImage(
                        task.id,
                        image.path,
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