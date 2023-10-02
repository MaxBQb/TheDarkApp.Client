package lab.maxb.dark.data.local.datasource

import lab.maxb.dark.data.local.dao.RecognitionTasksDAO
import lab.maxb.dark.data.local.model.RecognitionTaskLocalDTO
import org.koin.core.annotation.Singleton

@Singleton
class RecognitionTasksRoomDataSource(
    private val dao: RecognitionTasksDAO,
) : RecognitionTasksLocalDataSource, BaseRoomDataSource<RecognitionTaskLocalDTO>(dao) {
    override fun getFavoritesPaged() = dao.getFavoritesPaged()
    override fun hasFavorites() = dao.hasFavorites()
    override suspend fun saveOnly(vararg value: RecognitionTaskLocalDTO)
        = dao.saveOnly(*value)
    override fun getAll() = dao.getAll()
    override fun getAllPaged() = dao.getAllPaged()
    override fun get(id: String) = dao.get(id)
}