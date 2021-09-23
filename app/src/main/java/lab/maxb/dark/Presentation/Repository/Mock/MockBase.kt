package lab.maxb.dark.Presentation.Repository.Mock

import lab.maxb.dark.Domain.Model.RecognitionTask
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import lab.maxb.dark.Domain.Model.User
import lab.maxb.dark.Presentation.Repository.RepositoryTasks

class MockBase : RepositoryTasks {
    private var data: MutableList<RecognitionTask>? = mutableListOf(
        RecognitionTask(
            setOf("Пусто", "пусто", "пустота"),
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888),
            User("Максим", 0)
        )
    )
    private val recognitionTasks: MutableLiveData<List<RecognitionTask>?> = MutableLiveData(data)

    private fun reloadAllRecognitionTasks() {
        recognitionTasks.value = data
    }

    override fun getAllRecognitionTasks(): LiveData<List<RecognitionTask>?>
        = recognitionTasks

    override suspend fun <T : RecognitionTask> addRecognitionTask(task: T) {
        data!!.add(task)
        reloadAllRecognitionTasks()
    }

    override suspend fun <T : RecognitionTask> deleteRecognitionTask(task: T) {
        data!!.remove(task)
        reloadAllRecognitionTasks()
    }
}