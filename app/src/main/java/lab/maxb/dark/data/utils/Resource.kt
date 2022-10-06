package lab.maxb.dark.data.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import lab.maxb.dark.data.remote.dark.UnableToObtainResource

open class Resource<Input, Output>(
    var refreshController: RefreshController = RefreshControllerImpl()
) {
    open lateinit var fetchLocal: suspend (Input) -> Flow<Output?>
    open var fetchLocalSnapshot: suspend (Input) -> Output? = {
        fetchLocal(it).firstOrNull()
    }
    open lateinit var fetchRemote: suspend (Input) -> Output?
    open var isFresh: (suspend (Input, Output?) -> Boolean) = { it, cache ->
        !refreshController.isExpired() && !isEmptyCache(cache)
    }
    open var isEmptyResponse: (suspend (Output?) -> Boolean) = { it == null }
    open var isEmptyCache: (suspend (Output?) -> Boolean) = { it == null }
    open var onRefresh: (suspend () -> Unit)? = {
        refreshController.refresh()
    }
    open var localStore: (suspend (Output) -> Unit)? = null
    open var clearLocalStore: (suspend (Input) -> Unit)? = null

    fun query(args: Input, force: Boolean = false, useCache: Boolean = false) = flow {
        val cache = fetchLocalSnapshot(args)

        if (useCache && !isEmptyCache(cache))
            emit(cache)

        if (force || !isFresh(args, cache))
            refresh(args)

        emitAll(fetchLocal(args))
    }

    suspend fun refresh(args: Input) = try {
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

    suspend fun checkIsFresh(args: Input)
        = isFresh(args, fetchLocalSnapshot(args))
}