package lab.maxb.dark.data.local.datasource

import lab.maxb.dark.data.local.dao.ArticlesDAO
import lab.maxb.dark.data.local.model.ArticleLocalDTO
import org.koin.core.annotation.Singleton

@Singleton
class ArticlesRoomDataSource(
    private val dao: ArticlesDAO,
) : ArticlesLocalDataSource, BaseRoomDataSource<ArticleLocalDTO>(dao) {
    override fun getAll() = dao.getAll()
    override fun getAllPaged() = dao.getAllPaged()
    override fun get(id: String) = dao.get(id)
}