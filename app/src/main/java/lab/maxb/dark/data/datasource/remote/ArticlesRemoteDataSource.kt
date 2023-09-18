package lab.maxb.dark.data.datasource.remote

import lab.maxb.dark.data.model.remote.ArticleCreationNetworkDTO
import lab.maxb.dark.data.model.remote.ArticleNetworkDTO

interface ArticlesRemoteDataSource {
    suspend fun getAllArticles(page: Int, size: Int): List<ArticleNetworkDTO>?
    suspend fun updateArticle(id: String, article: ArticleCreationNetworkDTO): ArticleNetworkDTO?
    suspend fun addArticle(article: ArticleCreationNetworkDTO): ArticleNetworkDTO?
}
