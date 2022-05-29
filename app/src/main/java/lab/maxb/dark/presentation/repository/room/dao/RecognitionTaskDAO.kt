package lab.maxb.dark.presentation.repository.room.dao

import androidx.paging.PagingSource
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.presentation.repository.room.model.RecognitionTaskDTO
import lab.maxb.dark.presentation.repository.room.model.UserDTO
import lab.maxb.dark.presentation.repository.room.relations.RecognitionTaskWithOwner

@Dao
interface RecognitionTaskDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRecognitionTask(task: RecognitionTaskDTO): Long

    @Update
    suspend fun updateRecognitionTask(task: RecognitionTaskDTO)

    @Transaction
    suspend fun save(task: RecognitionTaskDTO) {
        if (addRecognitionTask(task) == -1L)
            updateRecognitionTask(task)
    }

    @Delete
    suspend fun deleteRecognitionTask(task: RecognitionTaskDTO)

    @Query("DELETE FROM recognition_task WHERE id=:id")
    suspend fun deleteRecognitionTask(id: String)

    @Transaction
    @Query("SELECT * FROM recognition_task ORDER BY reviewed")
    fun getAllRecognitionTasks(): Flow<List<RecognitionTaskWithOwner>?>

    @Transaction
    @Query("SELECT * FROM recognition_task ORDER BY reviewed")
    fun getAllRecognitionTasksPaged(): PagingSource<Int, RecognitionTaskWithOwner>

    @Transaction
    @Query("SELECT * FROM recognition_task WHERE id = :id")
    fun getRecognitionTask(id: String): Flow<RecognitionTaskDTO?>

    @Transaction
    @Query("DELETE FROM recognition_task")
    suspend fun clear()
}