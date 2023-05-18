package lab.maxb.dark.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.domain.model.Article
import lab.maxb.dark.domain.repository.utils.Resource

interface ArticlesRepository {
    val articleResource: Resource<String, Article>
    fun getAllArticles(): Flow<PagingData<Article>>
    suspend fun addArticle(article: Article)
    suspend fun updateArticle(article: Article)
}