package lab.maxb.dark.data.utils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import lab.maxb.dark.data.UnableToObtainResource
import lab.maxb.dark.domain.model.Mapper
import lab.maxb.dark.domain.model.NullableMapper
import lab.maxb.dark.domain.repository.utils.Resource

open class ResourceImpl<Input, Output, LocalOutput>(
    protected open val refreshController: RefreshController<LocalOutput> = InMemRefreshController(),
    protected open val fetchLocal: suspend (Input) -> Flow<LocalOutput?>,
    protected open val localMapper: NullableMapper<LocalOutput, Output>,
    protected open val reversedLocalMapper: Mapper<Output, LocalOutput>,
    fetchLocalSnapshot: (suspend (Input) -> LocalOutput?)? = null,
    protected open val fetchRemote: suspend (Input) -> Output?,
    isFresh: (suspend (Input, LocalOutput?) -> Boolean)? = null,
    protected open val isEmptyResponse: (suspend (Output?) -> Boolean) = { it == null },
    protected open val isEmptyCache: (suspend (LocalOutput?) -> Boolean) = { it == null },
    onRefresh: (suspend () -> Unit)? = null,
    protected open val localStore: (suspend (LocalOutput) -> Unit)? = null,
    protected open val clearLocalStore: (suspend (Input) -> Unit)? = null,
) : Resource<Input, Output> {
    protected open var cachedArgs: Input? = null

    protected open val fetchLocalSnapshot: suspend (Input) -> LocalOutput?
            = fetchLocalSnapshot ?: {
        fetchLocal(it).firstOrNull()
    }

    protected open val isFresh: (suspend (Input, LocalOutput?) -> Boolean)
            = isFresh ?: { _, cache ->
        !refreshController.isExpired(cache) && !isEmptyCache(cache)
    }

    protected open val onRefresh: (suspend () -> Unit)? = onRefresh ?: {
        refreshController.refresh()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun query(args: Input, force: Boolean, useCache: Boolean): Flow<Output?> = flow {
        val cache = fetchLocalSnapshot(args)
        cachedArgs = args

        if (useCache && !isEmptyCache(cache))
            emit(localMapper.map(cache))

        if (force || !isFresh(args, cache))
            refresh(args)

        emitAll(fetchLocal(args).distinctUntilChanged().mapLatest { localMapper.map(it) })
    }

    override suspend fun refresh(args: Input) = try {
        cachedArgs = args
        fetchRemote(args).also {
            if (isEmptyResponse(it))
                clearLocalStore?.invoke(args)
            else
                localStore?.invoke(reversedLocalMapper.map(it!!))
        }
        onRefresh?.invoke()
        true
    } catch (e: UnableToObtainResource) {
        false
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun retry() = refresh(cachedArgs as Input)

    override suspend fun checkIsFresh(args: Input)
        = isFresh(args, fetchLocalSnapshot(args))
}