package lab.maxb.dark.Presentation.Repository.Room.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.runBlocking
import lab.maxb.dark.Presentation.Repository.Room.Model.RecognitionTaskDTO
import lab.maxb.dark.Presentation.Repository.Room.Model.RecognitionTaskImage
import lab.maxb.dark.Presentation.Repository.Room.Model.RecognitionTaskName
import lab.maxb.dark.Presentation.Repository.Room.Relation.RecognitionTaskWithNamesAndImages
import lab.maxb.dark.Presentation.Repository.Room.Relation.RecognitionTaskWithOwnerAndImage

@Dao
interface RecognitionTaskDAO {
    @Insert
    suspend fun addRecognitionTask(task: RecognitionTaskDTO)

    @Insert
    suspend fun addRecognitionTaskNames(names: List<RecognitionTaskName>)

    @Insert
    suspend fun addRecognitionTaskImages(images: List<RecognitionTaskImage>)

    @Transaction
    fun addRecognitionTask(task: RecognitionTaskDTO,
                           names: List<RecognitionTaskName>,
                           images: List<RecognitionTaskImage>) = runBlocking {
        addRecognitionTask(task)
        addRecognitionTaskNames(names)
        addRecognitionTaskImages(images)
    }

    @Update
    suspend fun updateRecognitionTask(task: RecognitionTaskDTO)

    @Delete
    suspend fun deleteRecognitionTask(task: RecognitionTaskDTO)

    @Transaction
    @Query("SELECT * FROM recognition_task ORDER BY reviewed")
    fun getAllRecognitionTasks(): LiveData<List<RecognitionTaskWithOwnerAndImage>?>

    @Transaction
    @Query("SELECT * FROM recognition_task WHERE reviewed = :isReviewed")
    fun getAllRecognitionTasksByReview(isReviewed: Boolean): LiveData<List<RecognitionTaskWithOwnerAndImage>?>

    @Transaction
    @Query("SELECT * FROM recognition_task WHERE id = :id")
    fun getRecognitionTask(id: String): LiveData<RecognitionTaskWithNamesAndImages?>
}