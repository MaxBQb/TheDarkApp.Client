package lab.maxb.dark.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.data.local.model.RecognitionTaskLocalDTO
import lab.maxb.dark.data.local.relations.FullRecognitionTaskDTO

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

    @Transaction
    @Query("SELECT * FROM recognition_task WHERE favorite")
    abstract fun getFavoritesPaged(): PagingSource<Int, FullRecognitionTaskDTO>

    @Transaction
    @Query("SELECT EXISTS (SELECT 1 FROM recognition_task WHERE favorite)")
    abstract fun hasFavorites(): Flow<Boolean>

    @Query("SELECT * FROM recognition_task WHERE id = :id")
    abstract fun get(id: String): Flow<RecognitionTaskLocalDTO?>

    @Query("DELETE FROM recognition_task WHERE NOT (id in (:id))")
    abstract suspend fun deleteOther(id: List<String>)

    @Transaction
    open suspend fun saveOnly(vararg value: RecognitionTaskLocalDTO) {
        save(*value)
        deleteOther(value.map { it.id })
    }

    override suspend fun RecognitionTaskLocalDTO.withLocalsPreserved(
        old: RecognitionTaskLocalDTO
    ) = copy(
        favorite = favorite ?: old.favorite
    )

    override suspend fun RecognitionTaskLocalDTO.withLocalsDefault() = copy(
        favorite = favorite ?: false
    )
}