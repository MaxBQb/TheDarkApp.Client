package lab.maxb.dark.data.local.datasource

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.data.local.model.RecognitionTaskLocalDTO
import lab.maxb.dark.data.local.relations.FullRecognitionTaskDTO

interface RecognitionTasksLocalDataSource :
    BasePagedLocalDataSource<RecognitionTaskLocalDTO, FullRecognitionTaskDTO> {
    fun getFavoritesPaged(): PagingSource<Int, FullRecognitionTaskDTO>
    fun hasFavorites(): Flow<Boolean>
    suspend fun saveOnly(vararg value: RecognitionTaskLocalDTO)
}
