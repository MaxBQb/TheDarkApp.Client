package lab.maxb.dark.data.datasource.local

import lab.maxb.dark.data.local.room.dao.AdvancedDAO
import lab.maxb.dark.data.model.local.BaseLocalDTO

abstract class BaseRoomDataSource<T : BaseLocalDTO>(
    private val dao: AdvancedDAO<T>
) : BaseLocalDataSource<T> {
    override suspend fun getById(id: String) = dao.getById(id)
    override suspend fun delete(id: String) { dao.delete(id) }
    override suspend fun delete(vararg value: T) = dao.delete(*value)
    override suspend fun clear() { dao.clear() }
    override suspend fun save(value: T) = dao.save(value)
    override suspend fun save(vararg value: T) = dao.save(*value)
}