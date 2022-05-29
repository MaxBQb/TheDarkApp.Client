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
import lab.maxb.dark.presentation.repository.interfaces.RecognitionTasksRepository
import lab.maxb.dark.presentation.repository.interfaces.UsersRepository
import lab.maxb.dark.presentation.repository.network.dark.DarkService
import lab.maxb.dark.presentation.repository.network.dark.model.RecognitionTaskCreationDTO
import lab.maxb.dark.presentation.repository.room.LocalDatabase
import lab.maxb.dark.presentation.repository.room.dao.RecognitionTaskDAO
import lab.maxb.dark.presentation.repository.room.model.RecognitionTaskDTO
import lab.maxb.dark.presentation.repository.utils.Resource
import lab.maxb.dark.presentation.repository.utils.pagination.Page
import lab.maxb.dark.presentation.repository.utils.pagination.RecognitionTaskMediator
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
                    it.image?.let { x -> listOf(x) },
                    usersRepository.getUser(it.owner_id).firstOrNull()!!,
                    it.reviewed,
                    it.id,
                )
            }
        }
        localStore = {
            db.withTransaction {
                it.forEach { task ->
                    mRecognitionTaskDao.save(
                        RecognitionTaskDTO(task)
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

        taskLocal.images = task.images!!.map {
            mDarkService.addImage(
                taskLocal.id,
                imageLoader.fromUri(it.toUri())
            )!!
        }

        mRecognitionTaskDao.save(taskLocal)
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
            mDarkService.solveTask(id, answer)
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
                    task.images,
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
            mRecognitionTaskDao.save(RecognitionTaskDTO(task))
        }
        clearLocalStore = {
            mRecognitionTaskDao.deleteRecognitionTask(it)
        }
    }

    override suspend fun getRecognitionTask(id: String, forceUpdate: Boolean)
        = taskResource.query(id, forceUpdate)

    override fun getRecognitionTaskImage(path: String)
        = mDarkService.getImageSource(path)
}