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
import lab.maxb.dark.data.local.datasource.RecognitionTasksLocalDataSource
import lab.maxb.dark.data.local.datasource.RemoteKeysLocalDataSource
import lab.maxb.dark.data.remote.datasource.ImagesRemoteDataSource
import lab.maxb.dark.data.remote.datasource.RecognitionTasksRemoteDataSource
import lab.maxb.dark.data.local.relations.toDomain
import lab.maxb.dark.data.local.model.RecognitionTaskLocalDTO
import lab.maxb.dark.data.local.model.toDomain
import lab.maxb.dark.data.local.model.toLocalDTO
import lab.maxb.dark.data.remote.model.toDomain
import lab.maxb.dark.data.remote.model.toNetworkDTO
import lab.maxb.dark.data.utils.DbRefreshController
import lab.maxb.dark.data.utils.ImageLoader
import lab.maxb.dark.data.utils.ResourceImpl
import lab.maxb.dark.domain.model.Page
import lab.maxb.dark.data.pagination.RecognitionTaskMediator
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.domain.repository.RecognitionTasksRepository
import lab.maxb.dark.domain.repository.UsersRepository
import org.koin.core.annotation.Single

@Single
class RecognitionTasksRepositoryImpl(
    private val remoteDataSource: RecognitionTasksRemoteDataSource,
    private val imagesRemoteDataSource: ImagesRemoteDataSource,
    private val usersRepository: UsersRepository,
    private val imageLoader: ImageLoader,
    private val localDataSource: RecognitionTasksLocalDataSource,
    remoteKeys: RemoteKeysLocalDataSource,
) : RecognitionTasksRepository {

    private val tasksResource = ResourceImpl<Page, List<RecognitionTask>, List<RecognitionTaskLocalDTO>>(
        fetchLocal = { localDataSource.getAll() },
        localMapper = { x -> x?.map { it.toDomain() } },
        fetchRemote = { page ->
            remoteDataSource.getAllTasks(page.page, page.size)?.map {
                it.toDomain()
            }?.also { tasks ->
                coroutineScope {
                    tasks.map {
                        async { getUser(it.owner.id) }
                    }.awaitAll()
                }
            }
        },
        isEmptyResponse = { it.isNullOrEmpty() },
        isEmptyCache = { it.isNullOrEmpty() },
        localStore = { localDataSource.saveOnly(*it.toTypedArray()) },
        reversedLocalMapper = { tasks -> tasks.map { it.toLocalDTO() } },
        clearLocalStore = { page ->
            if (page.page == 0)
                localDataSource.clear()
        },
    )

    @OptIn(ExperimentalPagingApi::class)
    private val pager = Pager(
        config = PagingConfig(pageSize = 50),
        remoteMediator = RecognitionTaskMediator(tasksResource, remoteKeys),
    ) {
        localDataSource.getAllPaged()
    }.flow.map { page ->
        page.map { it.toDomain() }
    }

    private val favoritePager = Pager(
        config = PagingConfig(pageSize = 50),
    ) {
        localDataSource.getFavoritesPaged()
    }.flow.map { page ->
        page.map { it.toDomain() }
    }

    override fun getAllRecognitionTasks()
        = pager.map { it.toDomain() }
    override fun getFavoriteRecognitionTasks()
        = favoritePager.map { it.toDomain() }
    override fun hasFavoriteRecognitionTasks() = localDataSource.hasFavorites()

    override suspend fun addRecognitionTask(task: RecognitionTask) {
        val newTask = task.copy(images=task.images.map {
            imagesRemoteDataSource.addImage(
                imageLoader.fromUri(it.toUri())
            )!!
        }).toNetworkDTO()
        val taskResponse = remoteDataSource.addTask(newTask)!!
        val taskLocal = taskResponse.toDomain().toLocalDTO()
        localDataSource.save(taskLocal)
    }

    override suspend fun markRecognitionTask(task: RecognitionTask)
        = try {
            remoteDataSource.markTask(task.id, task.reviewed).also {
                if (it) recognitionTaskResource.refresh(task.id)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            false
        }

    override suspend fun markFavoriteRecognitionTask(task: RecognitionTask) {
        localDataSource.save(task.toLocalDTO())
    }

    override suspend fun solveRecognitionTask(id: String, answer: String)
        = try {
            remoteDataSource.solveTask(id, answer)
        } catch (e: Throwable) {
            e.printStackTrace()
            false
        }

    override val recognitionTaskResource = ResourceImpl(
        refreshController = DbRefreshController(),
        fetchRemote = { id ->
            remoteDataSource.getTask(id)?.toDomain()?.also {
                getUser(it.owner.id)
            }
        },
        fetchLocal = localDataSource::get,
        localMapper = { it?.toDomain() },
        reversedLocalMapper = { it.toLocalDTO() },
        localStore = localDataSource::save,
        clearLocalStore = localDataSource::delete,
    )

    override fun getRecognitionTaskImage(path: String)
        = imagesRemoteDataSource.getImageSource(path)

    private suspend fun getUser(id: String)
        = usersRepository.userResource.query(id).firstOrNull()!!
}