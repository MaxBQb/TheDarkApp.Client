package lab.maxb.dark.data.local.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.data.local.room.relations.FullRecognitionTaskDTO
import lab.maxb.dark.data.model.local.RecognitionTaskLocalDTO

@Dao
abstract class RecognitionTasksDAO: AdvancedDAO<RecognitionTaskLocalDTO>(
    "recognition_task"
) {
    @Transaction
    @Query("SELECT * FROM recognition_task ORDER BY reviewed ASC, id ASC")
    abstract fun getAll(): Flow<List<RecognitionTaskLocalDTO>?>

    @Transaction
    @Query("SELECT * FROM recognition_task ORDER BY reviewed ASC, id ASC")
    abstract fun getAllPaged(): PagingSource<Int, FullRecognitionTaskDTO>

    @Query("SELECT * FROM recognition_task WHERE id = :id")
    abstract fun get(id: String): Flow<RecognitionTaskLocalDTO?>

    @Query("DELETE FROM recognition_task WHERE NOT (id in (:id))")
    abstract fun deleteOther(id: List<String>)

    @Transaction
    open suspend fun saveOnly(vararg value: RecognitionTaskLocalDTO) {
        save(*value)
        deleteOther(value.map { it.id })
    }
}