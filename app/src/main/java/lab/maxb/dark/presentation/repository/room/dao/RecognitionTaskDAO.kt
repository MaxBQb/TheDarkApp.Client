package lab.maxb.dark.presentation.repository.room.dao

import androidx.paging.PagingSource
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.presentation.repository.room.model.RecognitionTaskDTO
import lab.maxb.dark.presentation.repository.room.model.RecognitionTaskName
import lab.maxb.dark.presentation.repository.room.relations.RecognitionTaskWithNamesAndImages
import lab.maxb.dark.presentation.repository.room.relations.RecognitionTaskWithOwnerAndImage

@Dao
interface RecognitionTaskDAO {
    @Insert(onConflict = REPLACE)
    suspend fun addRecognitionTask(task: RecognitionTaskDTO)

    @Insert(onConflict = REPLACE)
    suspend fun addRecognitionTaskNames(names: List<RecognitionTaskName>)

    @Transaction
    suspend fun addRecognitionTask(task: RecognitionTaskDTO,
                           names: List<RecognitionTaskName>) {
        deleteRecognitionTask(task)
        addRecognitionTask(task)
        addRecognitionTaskNames(names)
    }

    @Update
    suspend fun updateRecognitionTask(task: RecognitionTaskDTO)

    @Delete
    suspend fun deleteRecognitionTask(task: RecognitionTaskDTO)

    @Query("DELETE FROM recognition_task WHERE id=:id")
    suspend fun deleteRecognitionTask(id: String)

    @Transaction
    @Query("SELECT * FROM recognition_task ORDER BY reviewed")
    fun getAllRecognitionTasks(): Flow<List<RecognitionTaskWithOwnerAndImage>?>

    @Transaction
    @Query("SELECT * FROM recognition_task ORDER BY reviewed")
    fun getAllRecognitionTasksPaged(): PagingSource<Int, RecognitionTaskWithOwnerAndImage>

    @Transaction
    @Query("SELECT * FROM recognition_task WHERE id = :id")
    fun getRecognitionTask(id: String): Flow<RecognitionTaskWithNamesAndImages?>

    @Transaction
    @Query("DELETE FROM recognition_task")
    suspend fun clear()
}