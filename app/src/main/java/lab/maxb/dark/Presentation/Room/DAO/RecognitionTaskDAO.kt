package lab.maxb.dark.Presentation.Room.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.runBlocking
import lab.maxb.dark.Presentation.Room.Model.RecognitionTaskDTO
import lab.maxb.dark.Presentation.Room.Model.RecognitionTaskName
import lab.maxb.dark.Presentation.Room.Relation.RecognitionTaskWithOwner

@Dao
interface RecognitionTaskDAO {
    @Insert
    suspend fun addRecognitionTask(task: RecognitionTaskDTO)

    @Insert
    suspend fun addRecognitionTaskNames(name: List<RecognitionTaskName>)

    @Transaction
    fun addRecognitionTask(task: RecognitionTaskDTO,
                           names: List<RecognitionTaskName>) = runBlocking {
        addRecognitionTask(task)
        addRecognitionTaskNames(names)
    }

    @Delete
    suspend fun deleteRecognitionTask(task: RecognitionTaskDTO)

    @Transaction
    @Query("SELECT * FROM recognition_task")
    fun getAllRecognitionTasks(): LiveData<List<RecognitionTaskWithOwner>?>
}