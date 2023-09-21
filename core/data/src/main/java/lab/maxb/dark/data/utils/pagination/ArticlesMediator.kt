package lab.maxb.dark.data.utils.pagination

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.flow.firstOrNull
import lab.maxb.dark.data.datasource.local.RemoteKeysLocalDataSource
import lab.maxb.dark.data.model.local.ArticleLocalDTO
import lab.maxb.dark.data.model.local.RemoteKey
import lab.maxb.dark.domain.model.Article
import lab.maxb.dark.domain.repository.utils.Resource
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException


@OptIn(ExperimentalPagingApi::class)
class ArticleMediator(
    private val resource: Resource<Page, List<Article>>,
    private val remoteKeys: RemoteKeysLocalDataSource,
) : RemoteMediator<Int, ArticleLocalDTO>() {

    override suspend fun initialize() = if (
        resource.checkIsFresh(Page(0, 1))
        && remoteKeys.hasContent()
    )
        InitializeAction.SKIP_INITIAL_REFRESH
    else
        InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleLocalDTO>
    ): MediatorResult {
        return try {
            val pageKeyData = getKeyPageData(if (remoteKeys.hasContent())
                loadType else LoadType.REFRESH, state)
            val page = when (pageKeyData) {
                is MediatorResult.Success -> return pageKeyData
                else -> pageKeyData as Int
            }
            val response = resource.query(Page(page, state.config.pageSize), true).firstOrNull()
            val isEndOfList = response.isNullOrEmpty()
            if (loadType == LoadType.REFRESH)
                remoteKeys.clear()

            response?.map {
                RemoteKey(
                    it.id,
                    if (page == 0) null else page - 1,
                    if (isEndOfList) null else page + 1
                )
            }?.toTypedArray()?.let {
                remoteKeys.save(*it)
            }

            MediatorResult.Success(
                endOfPaginationReached = response?.isEmpty() ?: true
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, ArticleLocalDTO>): RemoteKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { article -> remoteKeys.getById(article.id) }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, ArticleLocalDTO>): RemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { article -> remoteKeys.getById(article.id) }
    }

    private suspend fun getClosestRemoteKey(state: PagingState<Int, ArticleLocalDTO>): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeys.getById(id)
            }
        }
    }

    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, ArticleLocalDTO>): Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: 0
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                    ?: throw InvalidObjectException("Remote key should not be null for $loadType")
                remoteKeys.nextKey
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                    ?: throw InvalidObjectException("Invalid state, key should not be null")
                //end of list condition reached
                remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                remoteKeys.prevKey
            }
        }
    }
}