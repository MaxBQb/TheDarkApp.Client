package lab.maxb.dark.data.repository

import androidx.core.net.toUri
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import lab.maxb.dark.data.local.room.LocalDatabase
import lab.maxb.dark.data.local.room.relations.toDomain
import lab.maxb.dark.data.model.local.toDomain
import lab.maxb.dark.data.model.local.toLocalDTO
import lab.maxb.dark.data.model.remote.toDomain
import lab.maxb.dark.data.model.remote.toNetworkDTO
import lab.maxb.dark.data.remote.dark.DarkService
import lab.maxb.dark.data.utils.Resource
import lab.maxb.dark.data.utils.pagination.Page
import lab.maxb.dark.data.utils.pagination.RecognitionTaskMediator
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.domain.repository.RecognitionTasksRepository
import lab.maxb.dark.domain.repository.UsersRepository
import lab.maxb.dark.presentation.extra.ImageLoader
import org.koin.core.annotation.Single

@Single
class RecognitionTasksRepositoryImpl(
    db: LocalDatabase,
    private val networkDataSource: DarkService,
    private val usersRepository: UsersRepository,
    private val imageLoader: ImageLoader,
) : RecognitionTasksRepository {
    private val localDataSource = db.recognitionTasks()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val tasksResource = Resource<Page, List<RecognitionTask>>().apply {
        fetchLocal = { _ ->
            localDataSource.getAll().mapLatest {
                data -> data?.map { it.toDomain() }
            }
        }
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
        }
        isEmptyResponse = { it.isNullOrEmpty() }
        isEmptyCache = { it.isNullOrEmpty() }
        localStore = { tasks ->
            tasks.map {
                it.toLocalDTO()
            }.toTypedArray().let {
                localDataSource.saveOnly(*it)
            }
        }
        clearLocalStore = { page ->
            if (page.page == 0)
                localDataSource.clear()
        }
    }

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
                if (it) refresh(task.id)
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private val taskResource = Resource<String, RecognitionTask>().apply {
        fetchRemote = { id ->
            networkDataSource.getTask(id)?.toDomain()?.also {
                getUser(it.ownerId)
            }
        }
        fetchLocal = { id ->
            localDataSource.get(id).mapLatest {
                it?.toDomain()
            }
        }
        localStore = { task ->
            localDataSource.save(task.toLocalDTO())
        }
        clearLocalStore = {
            localDataSource.delete(it)
        }
    }

    override suspend fun getRecognitionTask(id: String, forceUpdate: Boolean)
        = taskResource.query(id, forceUpdate, true)

    override suspend fun refresh(id: String) {
        taskResource.refresh(id)
    }

    override fun getRecognitionTaskImage(path: String)
        = networkDataSource.getImageSource(path)

    private suspend fun getUser(id: String)
        = usersRepository.getUser(id).firstOrNull()!!
}