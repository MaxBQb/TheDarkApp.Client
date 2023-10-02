package lab.maxb.dark.data.remote.datasource

import lab.maxb.dark.data.remote.model.ArticleCreationNetworkDTO
import lab.maxb.dark.data.remote.model.ArticleNetworkDTO

interface ArticlesRemoteDataSource {
    suspend fun getAllArticles(page: Int, size: Int): List<ArticleNetworkDTO>?
    suspend fun updateArticle(id: String, article: ArticleCreationNetworkDTO): ArticleNetworkDTO?
    suspend fun addArticle(article: ArticleCreationNetworkDTO): ArticleNetworkDTO?
}
