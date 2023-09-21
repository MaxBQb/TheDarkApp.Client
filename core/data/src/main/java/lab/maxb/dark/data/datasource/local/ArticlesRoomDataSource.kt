package lab.maxb.dark.data.datasource.local

import lab.maxb.dark.data.local.room.dao.ArticlesDAO
import lab.maxb.dark.data.model.local.ArticleLocalDTO
import org.koin.core.annotation.Singleton

@Singleton
class ArticlesRoomDataSource(
    private val dao: ArticlesDAO,
) : ArticlesLocalDataSource, BaseRoomDataSource<ArticleLocalDTO>(dao) {
    override fun getAll() = dao.getAll()
    override fun getAllPaged() = dao.getAllPaged()
    override fun get(id: String) = dao.get(id)
}