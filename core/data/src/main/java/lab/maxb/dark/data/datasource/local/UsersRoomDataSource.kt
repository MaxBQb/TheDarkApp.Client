package lab.maxb.dark.data.datasource.local

import lab.maxb.dark.data.local.room.dao.UsersDAO
import lab.maxb.dark.data.model.local.UserLocalDTO
import org.koin.core.annotation.Singleton

@Singleton
class UsersRoomDataSource(
    private val dao: UsersDAO,
) : UsersLocalDataSource, BaseRoomDataSource<UserLocalDTO>(dao) {
    override fun get(id: String) = dao.get(id)
}