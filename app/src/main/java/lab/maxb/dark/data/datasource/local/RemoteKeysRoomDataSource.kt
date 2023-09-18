package lab.maxb.dark.data.datasource.local

import kotlinx.coroutines.flow.flow
import lab.maxb.dark.data.local.room.dao.RemoteKeysDAO
import lab.maxb.dark.data.model.local.RemoteKey
import org.koin.core.annotation.Singleton

@Singleton
class RemoteKeysRoomDataSource(
    private val dao: RemoteKeysDAO,
) : RemoteKeysLocalDataSource, BaseRoomDataSource<RemoteKey>(dao) {
    override suspend fun hasContent() = dao.hasContent()
    override fun get(id: String) = flow { emit(dao.getById(id)) }
}