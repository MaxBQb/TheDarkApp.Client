package lab.maxb.dark.Presentation.Repository.Implementation

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import kotlinx.coroutines.flow.first
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.Presentation.Extra.ImageLoader
import lab.maxb.dark.Presentation.Repository.Interfaces.RecognitionTasksRepository
import lab.maxb.dark.Presentation.Repository.Interfaces.UsersRepository
import lab.maxb.dark.Presentation.Repository.Network.Dark.DarkService
import lab.maxb.dark.Presentation.Repository.Network.Dark.Groups.downloadLink
import lab.maxb.dark.Presentation.Repository.Room.DAO.RecognitionTaskDAO
import lab.maxb.dark.Presentation.Repository.Room.LocalDatabase
import lab.maxb.dark.Presentation.Repository.Room.Model.RecognitionTaskDTO
import lab.maxb.dark.Presentation.Repository.Room.Model.RecognitionTaskImage
import lab.maxb.dark.Presentation.Repository.Room.Model.RecognitionTaskName
import lab.maxb.dark_api.Model.POJO.RecognitionTaskCreationDTO

class RecognitionTasksRepositoryImpl(
    db: LocalDatabase,
    private val mDarkService: DarkService,
    private val usersRepository: UsersRepository,
    private val imageLoader: ImageLoader,
) : RecognitionTasksRepository {
    private val mRecognitionTaskDao: RecognitionTaskDAO = db.recognitionTaskDao()

    override suspend fun getAllRecognitionTasksByReview(isReviewed: Boolean): LiveData<List<RecognitionTask>?>
        = mRecognitionTaskDao.getAllRecognitionTasksByReview(isReviewed)
        .distinctUntilChanged().map {
            data -> data?.map { it.toRecognitionTask() }
        }.also { refreshRecognitionTaskList() }

    override suspend fun getAllRecognitionTasks(): LiveData<List<RecognitionTask>?>
        = mRecognitionTaskDao
        .getAllRecognitionTasks()
        .distinctUntilChanged().map {
            data -> data?.map { it.toRecognitionTask() }
        }.also { refreshRecognitionTaskList() }

    private suspend fun refreshRecognitionTaskList() {
        try {
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
                if (task.image!! !in mRecognitionTaskDao.getRecognitionTaskImages(task.id))
                    mRecognitionTaskDao.addRecognitionTaskImages(
                        listOf(RecognitionTaskImage(task.id,
                            refreshImage(task.image!!)
                        ))
                    )
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private suspend fun refreshRecognitionTask(id: String) {
        try {
            mDarkService.getTask(id)?.let { task ->
                mRecognitionTaskDao.deleteRecognitionTask(task.id)
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
                    task.images!!.map { RecognitionTaskImage(
                        task.id, it
                    ) }
                )
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override suspend fun getRecognitionTask(id: String): LiveData<RecognitionTask?>
        = mRecognitionTaskDao.getRecognitionTask(id).distinctUntilChanged().map {
            it?.toRecognitionTask()
        }.also {
            refreshRecognitionTask(id)
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
        if (mDarkService.markTask(task.id, task.reviewed))
            refreshRecognitionTaskList()
    }

    override suspend fun <T : RecognitionTask> deleteRecognitionTask(task: T) {
        mRecognitionTaskDao.deleteRecognitionTask(
            task as RecognitionTaskDTO
        )
    }

    private suspend fun refreshImage(id: String)
        = try { imageLoader.fromResponse(
            mDarkService.downloadImage(
                downloadLink(id)
            ),
            id
        ) } catch (e: Throwable) {
            e.printStackTrace()
            id
        }
}