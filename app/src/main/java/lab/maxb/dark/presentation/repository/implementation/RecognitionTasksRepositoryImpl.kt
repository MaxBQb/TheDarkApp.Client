package lab.maxb.dark.presentation.repository.implementation

import androidx.core.net.toUri
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.presentation.extra.ImageLoader
import lab.maxb.dark.presentation.repository.interfaces.RecognitionTasksRepository
import lab.maxb.dark.presentation.repository.interfaces.UsersRepository
import lab.maxb.dark.presentation.repository.network.dark.DarkService
import lab.maxb.dark.presentation.repository.network.dark.model.toDomain
import lab.maxb.dark.presentation.repository.network.dark.model.toNetworkDTO
import lab.maxb.dark.presentation.repository.room.LocalDatabase
import lab.maxb.dark.presentation.repository.room.model.toDomain
import lab.maxb.dark.presentation.repository.room.model.toLocalDTO
import lab.maxb.dark.presentation.repository.room.relations.toDomain
import lab.maxb.dark.presentation.repository.utils.Resource
import lab.maxb.dark.presentation.repository.utils.pagination.Page
import lab.maxb.dark.presentation.repository.utils.pagination.RecognitionTaskMediator
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
        fetchLocal = { page ->
            localDataSource.getAll().mapLatest {
                data -> data?.map { it.toDomain() }
            }
        }
        fetchRemote = { page ->
            networkDataSource.getAllTasks(page.page, page.size)?.map {
                it.toDomain { getUser(it.owner_id) }
            }
        }
        localStore = { tasks ->
            tasks.map {
                it.toLocalDTO()
            }.toTypedArray().let {
                localDataSource.save(*it)
            }
        }
        clearLocalStore = { page ->
            if (page.page == 0)
                localDataSource.clear()
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    private val pager = Pager(
        config = PagingConfig(pageSize = 5),
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

        taskLocal.images = task.images!!.map {
            networkDataSource.addImage(
                taskLocal.id,
                imageLoader.fromUri(it.toUri())
            )!!
        }

        localDataSource.save(taskLocal)
    }

    override suspend fun markRecognitionTask(task: RecognitionTask) {
        localDataSource.update(task.toLocalDTO())
        try {
            if (networkDataSource.markTask(task.id, task.reviewed))
                getRecognitionTask(task.id, true).firstOrNull()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
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
            networkDataSource.getTask(id)?.let {
                it.toDomain { getUser(it.owner_id) }
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
        = taskResource.query(id, forceUpdate)

    override fun getRecognitionTaskImage(path: String)
        = networkDataSource.getImageSource(path)

    private suspend fun getUser(id: String) =
        usersRepository.getUser(id).firstOrNull()!!
}