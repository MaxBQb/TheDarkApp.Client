package lab.maxb.dark.presentation.repository.implementation

import androidx.core.net.toUri
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import androidx.room.withTransaction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.presentation.extra.ImageLoader
import lab.maxb.dark.presentation.repository.interfaces.ImagesRepository
import lab.maxb.dark.presentation.repository.interfaces.RecognitionTasksRepository
import lab.maxb.dark.presentation.repository.interfaces.UsersRepository
import lab.maxb.dark.presentation.repository.network.dark.DarkService
import lab.maxb.dark.presentation.repository.network.dark.model.RecognitionTaskCreationDTO
import lab.maxb.dark.presentation.repository.room.LocalDatabase
import lab.maxb.dark.presentation.repository.room.dao.RecognitionTaskDAO
import lab.maxb.dark.presentation.repository.room.model.RecognitionTaskDTO
import lab.maxb.dark.presentation.repository.room.model.RecognitionTaskImageCrossref
import lab.maxb.dark.presentation.repository.room.model.RecognitionTaskName
import lab.maxb.dark.presentation.repository.utils.Resource
import lab.maxb.dark.presentation.repository.utils.pagination.Page
import lab.maxb.dark.presentation.repository.utils.pagination.RecognitionTaskMediator
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
    private val tasksResource = Resource<Page, List<RecognitionTask>>().apply {
        fetchLocal = { page ->
            mRecognitionTaskDao.getAllRecognitionTasks().mapLatest {
                data -> data?.map { it.toRecognitionTask() }
            }
        }
        fetchRemote = { page ->
            mDarkService.getAllTasks(page.page, page.size)?.map {
                RecognitionTask(
                    setOf(),
                    imagesRepository.getById(it.image!!).firstOrNull()?.let { listOf(it) },
                    usersRepository.getUser(it.owner_id).firstOrNull()!!,
                    it.reviewed,
                    it.id,
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
                            task.images?.get(0)?.id ?: return@forEach,
                        ))
                    )
                }
            }
        }
        clearLocalStore = { page ->
            if (page.page == 0)
                mRecognitionTaskDao.clear()
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    private val pager = Pager(
        config = PagingConfig(pageSize = 5),
        remoteMediator = RecognitionTaskMediator(tasksResource, db.remoteKeysDao()),
    ) {
        mRecognitionTaskDao.getAllRecognitionTasksPaged()
    }.flow.map { page ->
        page.map {
            it.toRecognitionTask()
        }
    }

    override fun getAllRecognitionTasks() = pager

    override suspend fun addRecognitionTask(task: RecognitionTask) {
        val taskLocal = RecognitionTaskDTO(task)
        mDarkService.addTask(
            RecognitionTaskCreationDTO(
            task.names!!
        )
        )?.also { taskLocal.id = it }

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

    override suspend fun markRecognitionTask(task: RecognitionTask) {
        mRecognitionTaskDao.updateRecognitionTask(task as RecognitionTaskDTO)
        try {
            if (mDarkService.markTask(task.id, task.reviewed))
                getRecognitionTask(task.id, true).firstOrNull()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override suspend fun solveRecognitionTask(id: String, answer: String)
        = try {
            val result = mDarkService.solveTask(id, answer)
            if (result)
                getRecognitionTask(id, true).firstOrNull()
            result
        } catch (e: Throwable) {
            e.printStackTrace()
            false
        }

    override suspend fun deleteRecognitionTask(task: RecognitionTask) {
        mRecognitionTaskDao.deleteRecognitionTask(
            task as RecognitionTaskDTO
        )
    }

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
                    task.reviewed,
                    task.id,
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