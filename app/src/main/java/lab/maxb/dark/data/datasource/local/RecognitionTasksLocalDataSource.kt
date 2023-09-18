package lab.maxb.dark.data.datasource.local

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.data.local.room.relations.FullRecognitionTaskDTO
import lab.maxb.dark.data.model.local.RecognitionTaskLocalDTO

interface RecognitionTasksLocalDataSource :
    BasePagedLocalDataSource<RecognitionTaskLocalDTO, FullRecognitionTaskDTO> {
    fun getFavoritesPaged(): PagingSource<Int, FullRecognitionTaskDTO>
    fun hasFavorites(): Flow<Boolean>
    suspend fun saveOnly(vararg value: RecognitionTaskLocalDTO)
}
