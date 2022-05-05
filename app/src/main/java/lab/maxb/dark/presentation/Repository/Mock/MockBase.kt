package lab.maxb.dark.presentation.Repository.Mock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.Domain.Model.User
import lab.maxb.dark.presentation.Repository.Interfaces.RecognitionTasksRepository

class MockBase : RecognitionTasksRepository {
    private var data: MutableList<RecognitionTask>? = mutableListOf(
        RecognitionTask(
            setOf("Пусто", "пусто", "пустота"),
            listOf(""),
            User("Максим", 0)
        )
    )
    private val recognitionTasks: MutableLiveData<List<RecognitionTask>?> = MutableLiveData(data)

    private fun reloadAllRecognitionTasks() {
        recognitionTasks.value = data
    }

    override fun getAllRecognitionTasksByReview(isReviewed: Boolean): LiveData<List<RecognitionTask>?> {
        TODO("Not yet implemented")
    }

    override fun getAllRecognitionTasks(): LiveData<List<RecognitionTask>?>
        = recognitionTasks

    override suspend fun <T : RecognitionTask> addRecognitionTask(task: T) {
        data!!.add(task)
        reloadAllRecognitionTasks()
    }

    override suspend fun <T : RecognitionTask> markRecognitionTask(task: T) {
        TODO("Not yet implemented")
    }

    override suspend fun <T : RecognitionTask> deleteRecognitionTask(task: T) {
        data!!.remove(task)
        reloadAllRecognitionTasks()
    }

    override suspend fun clearCache()
        = TODO("Not yet implemented")

    override fun getRecognitionTask(id: String): LiveData<RecognitionTask?> {
        return MutableLiveData(data?.get(0))
    }
}