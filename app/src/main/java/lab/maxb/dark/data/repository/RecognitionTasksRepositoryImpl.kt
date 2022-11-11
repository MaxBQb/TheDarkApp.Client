package lab.maxb.dark.data.repository

import androidx.core.net.toUri
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import lab.maxb.dark.data.local.room.LocalDatabase
import lab.maxb.dark.data.local.room.relations.toDomain
import lab.maxb.dark.data.model.local.RecognitionTaskLocalDTO
import lab.maxb.dark.data.model.local.toDomain
import lab.maxb.dark.data.model.local.toLocalDTO
import lab.maxb.dark.data.model.remote.toDomain
import lab.maxb.dark.data.model.remote.toNetworkDTO
import lab.maxb.dark.data.remote.dark.DarkService
import lab.maxb.dark.data.utils.DbRefreshController
import lab.maxb.dark.data.utils.ResourceImpl
import lab.maxb.dark.data.utils.pagination.Page
import lab.maxb.dark.data.utils.pagination.RecognitionTaskMediator
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.domain.repository.RecognitionTasksRepository
import lab.maxb.dark.domain.repository.UsersRepository
import lab.maxb.dark.presentation.extra.ImageLoader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecognitionTasksRepositoryImpl @Inject constructor(
    db: LocalDatabase,
    private val networkDataSource: DarkService,
    private val usersRepository: UsersRepository,
    private val imageLoader: ImageLoader,
) : RecognitionTasksRepository {
    private val localDataSource = db.recognitionTasks()

    private val tasksResource = ResourceImpl<Page, List<RecognitionTask>, List<RecognitionTaskLocalDTO>>(
        fetchLocal = { localDataSource.getAll() },
        localMapper = { x -> x?.map { it.toDomain() } },
        fetchRemote = { page ->
            networkDataSource.getAllTasks(page.page, page.size)?.map {
                it.toDomain()
            }?.also {
                coroutineScope {
                    it.map {
                        async {
                            getUser(it.ownerId)
                        }
                    }.let { awaitAll(*it.toTypedArray()) }
                }
            }
        },
        isEmptyResponse = { it.isNullOrEmpty() },
        isEmptyCache = { it.isNullOrEmpty() },
        localStore = { tasks ->
            tasks.map {
                it.toLocalDTO()
            }.toTypedArray().let {
                localDataSource.saveOnly(*it)
            }
        },
        clearLocalStore = { page ->
            if (page.page == 0)
                localDataSource.clear()
        },
    )

    @OptIn(ExperimentalPagingApi::class)
    private val pager = Pager(
        config = PagingConfig(pageSize = 50),
        remoteMediator = RecognitionTaskMediator(tasksResource, db.remoteKeys()),
    ) {
        localDataSource.getAllPaged()
    }.flow.map { page ->
        page.map { it.toDomain() }
    }

    override fun getAllRecognitionTasks() = pager

    override suspend fun addRecognitionTask(task: RecognitionTask) {
        val taskLocal = task.toLocalDTO()
        networkDataSource.addTask(
            task.toNetworkDTO()
        )?.also { taskLocal.id = it }

        taskLocal.images = task.images.map {
            networkDataSource.addImage(
                taskLocal.id,
                imageLoader.fromUri(it.toUri())
            )!!
        }

        localDataSource.save(taskLocal)
    }

    override suspend fun markRecognitionTask(task: RecognitionTask)
        = try {
            networkDataSource.markTask(task.id, task.reviewed).also {
                if (it) recognitionTaskResource.refresh(task.id)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            false
        }

    override suspend fun solveRecognitionTask(id: String, answer: String)
        = try {
            networkDataSource.solveTask(id, answer)
        } catch (e: Throwable) {
            e.printStackTrace()
            false
        }

    override val recognitionTaskResource = ResourceImpl<String, RecognitionTask,
            RecognitionTaskLocalDTO>(
        refreshController = DbRefreshController(),
        fetchRemote = { id ->
            networkDataSource.getTask(id)?.toDomain()?.also {
                getUser(it.ownerId)
            }
        },
        fetchLocal = { localDataSource.get(it) },
        localMapper = { it?.toDomain() },
        localStore = { localDataSource.save(it.toLocalDTO()) },
        clearLocalStore = { localDataSource.delete(it) },
    )

    override fun getRecognitionTaskImage(path: String)
        = networkDataSource.getImageSource(path)

    private suspend fun getUser(id: String)
        = usersRepository.userResource.query(id).firstOrNull()!!
}