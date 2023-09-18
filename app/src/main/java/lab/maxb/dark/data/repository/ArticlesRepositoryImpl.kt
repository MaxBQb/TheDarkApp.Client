package lab.maxb.dark.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import lab.maxb.dark.data.datasource.ArticlesRemoteDataSource
import lab.maxb.dark.data.local.room.dao.ArticlesDAO
import lab.maxb.dark.data.local.room.dao.RemoteKeysDAO
import lab.maxb.dark.data.model.local.ArticleLocalDTO
import lab.maxb.dark.data.model.local.toDomain
import lab.maxb.dark.data.model.local.toLocalDTO
import lab.maxb.dark.data.model.remote.toDomain
import lab.maxb.dark.data.model.remote.toNetworkDTO
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
    private val remoteDataSource: ArticlesRemoteDataSource,
    private val usersRepository: UsersRepository,
    private val localDataSource: ArticlesDAO,
    remoteKeys: RemoteKeysDAO,
) : ArticlesRepository {

    private val articlesResource = ResourceImpl<Page, List<Article>, List<ArticleLocalDTO>>(
        fetchLocal = { localDataSource.getAll() },
        localMapper = { x -> x?.map { it.toDomain() } },
        reversedLocalMapper = { x -> x.map { it.toLocalDTO() } },
        fetchRemote = { page ->
            remoteDataSource.getAllArticles(page.page, page.size)?.map {
                it.toDomain()
            }?.also {
                coroutineScope {
                    it.map {
                        async {
                            getUser(it.author.id)
                        }
                    }.let { awaitAll(*it.toTypedArray()) }
                }
            }
        },
        isEmptyResponse = { it.isNullOrEmpty() },
        isEmptyCache = { it.isNullOrEmpty() },
        localStore = { localDataSource.save(*it.toTypedArray()) },
        clearLocalStore = { page ->
            if (page.page == 0)
                localDataSource.clear()
        },
    )

    @OptIn(ExperimentalPagingApi::class)
    private val pager = Pager(
        config = PagingConfig(pageSize = 50),
        remoteMediator = ArticleMediator(articlesResource, remoteKeys),
    ) {
        localDataSource.getAllPaged()
    }.flow.map { page ->
        page.map { it.toDomain() }
    }

    override fun getPagedArticles() = pager
    override fun getArticles() =
        articlesResource.query(Page(0, 50), useCache = true).filterNotNull()


    override suspend fun addArticle(article: Article) {
        val newArticle = article.toNetworkDTO()
        val response = remoteDataSource.addArticle(newArticle)!!
        val localModel = response.toDomain().toLocalDTO()
        localDataSource.save(localModel)
    }

    override suspend fun updateArticle(article: Article) {
        val requestModel = article.toNetworkDTO()
        val response = remoteDataSource.updateArticle(article.id, requestModel)!!
        val localModel = response.toDomain().toLocalDTO()
        localDataSource.save(localModel)
    }

    override val articleResource = ResourceImpl(
        refreshController = DbRefreshController(),
        fetchRemote = { id -> throw UnableToObtainResource() },
        fetchLocal = localDataSource::get,
        localMapper = { it?.toDomain() },
        reversedLocalMapper = { it.toLocalDTO() },
        localStore = localDataSource::save,
    )

    private suspend fun getUser(id: String)
        = usersRepository.userResource.query(id).firstOrNull()!!
}