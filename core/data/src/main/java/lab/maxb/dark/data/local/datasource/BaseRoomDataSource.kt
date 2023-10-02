package lab.maxb.dark.data.local.datasource

import lab.maxb.dark.data.local.dao.AdvancedDAO
import lab.maxb.dark.data.local.model.BaseLocalDTO

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