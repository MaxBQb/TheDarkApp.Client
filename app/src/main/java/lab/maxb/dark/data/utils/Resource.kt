package lab.maxb.dark.data.utils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import lab.maxb.dark.data.remote.dark.UnableToObtainResource

open class Resource<Input, Output, LocalOutput>(
    protected open var refreshController: RefreshController<LocalOutput> = InMemRefreshController()
) {
    protected open var cachedArgs: Input? = null
    open lateinit var fetchLocal: suspend (Input) -> Flow<LocalOutput?>
    open var localMapper: suspend (LocalOutput?) -> Output? = { it as Output? }
    open var fetchLocalSnapshot: suspend (Input) -> LocalOutput? = {
        fetchLocal(it).firstOrNull()
    }
    open lateinit var fetchRemote: suspend (Input) -> Output?
    open var isFresh: (suspend (Input, LocalOutput?) -> Boolean) = { _, cache ->
        !refreshController.isExpired(cache) && !isEmptyCache(cache)
    }
    open var isEmptyResponse: (suspend (Output?) -> Boolean) = { it == null }
    open var isEmptyCache: (suspend (LocalOutput?) -> Boolean) = { it == null }
    open var onRefresh: (suspend () -> Unit)? = {
        refreshController.refresh()
    }
    open var localStore: (suspend (Output) -> Unit)? = null
    open var clearLocalStore: (suspend (Input) -> Unit)? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    fun query(args: Input, force: Boolean = false, useCache: Boolean = false): Flow<Output?> = flow {
        val cache = fetchLocalSnapshot(args)
        cachedArgs = args

        if (useCache && !isEmptyCache(cache))
            emit(localMapper(cache))

        if (force || !isFresh(args, cache))
            refresh(args)

        emitAll(fetchLocal(args).distinctUntilChanged().mapLatest { localMapper(it) })
    }

    suspend fun refresh(args: Input) = try {
        cachedArgs = args
        fetchRemote(args).also {
            if (isEmptyResponse(it))
                clearLocalStore?.invoke(args)
            else
                localStore?.invoke(it!!)
        }
        onRefresh?.invoke()
        true
    } catch (e: UnableToObtainResource) {
        false
    }

    suspend fun retry() = refresh(cachedArgs!!)

    suspend fun checkIsFresh(args: Input)
        = isFresh(args, fetchLocalSnapshot(args))
}