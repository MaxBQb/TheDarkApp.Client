package lab.maxb.dark.data.local.datasource

import lab.maxb.dark.data.local.dao.UsersDAO
import lab.maxb.dark.data.local.model.UserLocalDTO
import org.koin.core.annotation.Singleton

@Singleton
class UsersRoomDataSource(
    private val dao: UsersDAO,
) : UsersLocalDataSource, BaseRoomDataSource<UserLocalDTO>(dao) {
    override fun get(id: String) = dao.get(id)
}