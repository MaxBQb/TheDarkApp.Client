package lab.maxb.dark.presentation.repository.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.presentation.repository.room.model.RecognitionTaskLocalDTO
import lab.maxb.dark.presentation.repository.room.relations.FullRecognitionTaskDTO

@Dao
abstract class RecognitionTasksDAO: AdvancedDAO<RecognitionTaskLocalDTO>(
    "recognition_task"
) {
    @Transaction
    @Query("SELECT * FROM recognition_task ORDER BY reviewed")
    abstract fun getAll(): Flow<List<FullRecognitionTaskDTO>?>

    @Transaction
    @Query("SELECT * FROM recognition_task ORDER BY reviewed")
    abstract fun getAllPaged(): PagingSource<Int, FullRecognitionTaskDTO>

    @Query("SELECT * FROM recognition_task WHERE id = :id")
    abstract fun get(id: String): Flow<FullRecognitionTaskDTO?>

    @Query("DELETE FROM recognition_task WHERE NOT (id in (:id))")
    abstract fun deleteOther(id: List<String>)

    @Transaction
    open suspend fun saveOnly(vararg value: RecognitionTaskLocalDTO) {
        save(*value)
        deleteOther(value.map { it.id })
    }
}