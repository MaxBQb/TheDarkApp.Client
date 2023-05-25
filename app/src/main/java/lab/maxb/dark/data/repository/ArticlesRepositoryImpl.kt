package lab.maxb.dark.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import lab.maxb.dark.data.local.room.LocalDatabase
import lab.maxb.dark.data.model.local.ArticleLocalDTO
import lab.maxb.dark.data.model.local.toDomain
import lab.maxb.dark.data.model.local.toLocalDTO
import lab.maxb.dark.data.model.remote.toDomain
import lab.maxb.dark.data.model.remote.toNetworkDTO
import lab.maxb.dark.data.remote.dark.DarkService
import lab.maxb.dark.data.remote.dark.UnableToObtainResource
import lab.maxb.dark.data.utils.DbRefreshController
import lab.maxb.dark.data.utils.ResourceImpl
import lab.maxb.dark.data.utils.pagination.ArticleMediator
import lab.maxb.dark.data.utils.pagination.Page
import lab.maxb.dark.domain.model.Article
import lab.maxb.dark.domain.repository.ArticlesRepository
import lab.maxb.dark.domain.repository.UsersRepository
import org.koin.core.annotation.Single

@Single
class ArticlesRepositoryImpl(
    db: LocalDatabase,
    private val networkDataSource: DarkService,
    private val usersRepository: UsersRepository,
) : ArticlesRepository {
    private val localDataSource = db.articles()

    private val articlesResource = ResourceImpl<Page, List<Article>, List<ArticleLocalDTO>>(
        fetchLocal = { localDataSource.getAll() },
        localMapper = { x -> x?.map { it.toDomain() } },
        fetchRemote = { page ->
            networkDataSource.getAllArticles(page.page, page.size)?.map {
                it.toDomain()
            }?.also {
                coroutineScope {
                    it.map {
                        async {
                            getUser(it.authorId)
                        }
                    }.let { awaitAll(*it.toTypedArray()) }
                }
            }
        },
        isEmptyResponse = { it.isNullOrEmpty() },
        isEmptyCache = { it.isNullOrEmpty() },
        localStore = { articles ->
            articles.map {
                it.toLocalDTO()
            }.toTypedArray().let {
                localDataSource.save(*it)
            }
        },
        clearLocalStore = { page ->
            if (page.page == 0)
                localDataSource.clear()
        },
    )

    @OptIn(ExperimentalPagingApi::class)
    private val pager = Pager(
        config = PagingConfig(pageSize = 50),
        remoteMediator = ArticleMediator(articlesResource, db.remoteKeys()),
    ) {
        localDataSource.getAllPaged()
    }.flow.map { page ->
        page.map { it.toDomain() }
    }

    override fun getAllArticles() = pager

    override suspend fun addArticle(article: Article) {
        val newArticle = article.toNetworkDTO()
        val response = networkDataSource.addArticle(newArticle)!!
        val localModel = response.toDomain().toLocalDTO()
        localDataSource.save(localModel)
    }

    override suspend fun updateArticle(article: Article) {
        val requestModel = article.toNetworkDTO()
        val response = networkDataSource.updateArticle(article.id, requestModel)!!
        val localModel = response.toDomain().toLocalDTO()
        localDataSource.save(localModel)
    }

    override val articleResource = ResourceImpl<String, Article, ArticleLocalDTO>(
        refreshController = DbRefreshController(),
        fetchRemote = { id -> throw UnableToObtainResource() },
        fetchLocal = { localDataSource.get(it) },
        localMapper = { it?.toDomain() },
        localStore = { localDataSource.save(it.toLocalDTO()) },
    )

    private suspend fun getUser(id: String)
        = usersRepository.userResource.query(id).firstOrNull()!!
}