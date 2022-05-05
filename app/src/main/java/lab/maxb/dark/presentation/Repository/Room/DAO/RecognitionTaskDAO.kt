package lab.maxb.dark.presentation.Repository.Room.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import lab.maxb.dark.presentation.Repository.Room.Model.RecognitionTaskDTO
import lab.maxb.dark.presentation.Repository.Room.Model.RecognitionTaskImage
import lab.maxb.dark.presentation.Repository.Room.Model.RecognitionTaskName
import lab.maxb.dark.presentation.Repository.Room.Relation.RecognitionTaskWithNamesAndImages
import lab.maxb.dark.presentation.Repository.Room.Relation.RecognitionTaskWithOwnerAndImage

@Dao
interface RecognitionTaskDAO {
    @Insert(onConflict = REPLACE)
    suspend fun addRecognitionTask(task: RecognitionTaskDTO)

    @Insert(onConflict = REPLACE)
    suspend fun addRecognitionTaskNames(names: List<RecognitionTaskName>)

    @Insert(onConflict = REPLACE)
    suspend fun addRecognitionTaskImages(images: List<RecognitionTaskImage>)

    @Transaction
    suspend fun addRecognitionTask(task: RecognitionTaskDTO,
                           names: List<RecognitionTaskName>,
                           images: List<RecognitionTaskImage>) {
        deleteRecognitionTask(task)
        addRecognitionTask(task)
        addRecognitionTaskNames(names)
        addRecognitionTaskImages(images)
    }

    @Update
    suspend fun updateRecognitionTask(task: RecognitionTaskDTO)

    @Delete
    suspend fun deleteRecognitionTask(task: RecognitionTaskDTO)

    @Transaction
    suspend fun deleteRecognitionTasks(exclude: List<String>) {
        getAllRecognitionTasksIds().forEach {
            if (it !in exclude)
                deleteRecognitionTask(it)
        }
    }

    @Query("DELETE FROM recognition_task WHERE id=:id")
    suspend fun deleteRecognitionTask(id: String)

    @Query("SELECT * FROM recognition_task_image WHERE recognition_task = :id")
    suspend fun getRecognitionTaskImages(id: String): List<RecognitionTaskImage>

    @Query("SELECT id FROM recognition_task")
    suspend fun getAllRecognitionTasksIds(): List<String>

    @Transaction
    @Query("SELECT * FROM recognition_task ORDER BY reviewed")
    fun getAllRecognitionTasks(): LiveData<List<RecognitionTaskWithOwnerAndImage>?>

    @Transaction
    @Query("SELECT * FROM recognition_task WHERE reviewed = :isReviewed")
    fun getAllRecognitionTasksByReview(isReviewed: Boolean): LiveData<List<RecognitionTaskWithOwnerAndImage>?>

    @Transaction
    @Query("SELECT * FROM recognition_task WHERE id = :id")
    fun getRecognitionTask(id: String): LiveData<RecognitionTaskWithNamesAndImages?>

    @Query("SELECT EXISTS(SELECT * FROM recognition_task WHERE id = :id)")
    suspend fun hasRecognitionTask(id: String): Boolean

    @Transaction
    @Query("DELETE FROM recognition_task")
    suspend fun clear()
}