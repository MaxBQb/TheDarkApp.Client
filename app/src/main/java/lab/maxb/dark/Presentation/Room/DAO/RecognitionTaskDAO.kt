package lab.maxb.dark.Presentation.Room.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import lab.maxb.dark.Presentation.Room.Model.RecognitionTaskDTO
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecognitionTaskDAO {
    @Insert
    fun addRecognitionTask(task: RecognitionTaskDTO?)

    @Delete
    fun deleteRecognitionTask(task: RecognitionTaskDTO?)

    @Query("SELECT * FROM recognition_task")
    fun getAllRecognitionTasks(): LiveData<List<RecognitionTaskDTO>?>
}