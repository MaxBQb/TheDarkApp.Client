package lab.maxb.dark.presentation.repository.utils.pagination

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.flow.firstOrNull
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.presentation.repository.room.dao.RemoteKeysDAO
import lab.maxb.dark.presentation.repository.room.model.RemoteKeys
import lab.maxb.dark.presentation.repository.room.relations.RecognitionTaskWithOwnerAndImage
import lab.maxb.dark.presentation.repository.utils.BaseResource
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException


@OptIn(ExperimentalPagingApi::class)
class RecognitionTaskMediator(
    private val resource: BaseResource<Page, List<RecognitionTask>>,
    private val remoteKeys: RemoteKeysDAO,
) : RemoteMediator<Int, RecognitionTaskWithOwnerAndImage>() {

    override suspend fun initialize() = if (resource.isFresh(Page(0, 1)))
        InitializeAction.SKIP_INITIAL_REFRESH
    else
        InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RecognitionTaskWithOwnerAndImage>
    ): MediatorResult {
        return try {
            val pageKeyData = getKeyPageData(loadType, state)
            val page = when (pageKeyData) {
                is MediatorResult.Success -> return pageKeyData
                else -> pageKeyData as Int
            }
            val response = resource.query(Page(page, state.config.pageSize), true).firstOrNull()
            val isEndOfList = response?.isEmpty() ?: true
            if (loadType == LoadType.REFRESH)
                remoteKeys.clear()

            response?.map {
                RemoteKeys(
                    it.id,
                    if (page == 0) null else page - 1,
                    if (isEndOfList) null else page + 1
                )
            }?.let {
                remoteKeys.save(it)
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

    private suspend fun getFirstRemoteKey(state: PagingState<Int, RecognitionTaskWithOwnerAndImage>): RemoteKeys? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { doggo -> remoteKeys.getById(doggo.recognition_task.id) }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, RecognitionTaskWithOwnerAndImage>): RemoteKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { doggo -> remoteKeys.getById(doggo.recognition_task.id) }
    }

    private suspend fun getClosestRemoteKey(state: PagingState<Int, RecognitionTaskWithOwnerAndImage>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.recognition_task?.id?.let { id ->
                remoteKeys.getById(id)
            }
        }
    }

    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, RecognitionTaskWithOwnerAndImage>): Any? {
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