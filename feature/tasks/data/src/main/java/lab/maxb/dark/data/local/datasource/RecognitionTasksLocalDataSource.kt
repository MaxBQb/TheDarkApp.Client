package lab.maxb.dark.data.local.datasource

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.data.local.relations.FullRecognitionTaskDTO
import lab.maxb.dark.data.local.model.RecognitionTaskLocalDTO

interface RecognitionTasksLocalDataSource :
    BasePagedLocalDataSource<RecognitionTaskLocalDTO, FullRecognitionTaskDTO> {
    fun getFavoritesPaged(): PagingSource<Int, FullRecognitionTaskDTO>
    fun hasFavorites(): Flow<Boolean>
    suspend fun saveOnly(vararg value: RecognitionTaskLocalDTO)
}
