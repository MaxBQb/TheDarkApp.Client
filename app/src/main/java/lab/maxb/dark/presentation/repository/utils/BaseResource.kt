package lab.maxb.dark.presentation.repository.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import lab.maxb.dark.presentation.repository.network.dark.UnableToObtainResource

open class BaseResource<Input, Output> {
    open lateinit var fetchLocal: suspend (Input) -> Flow<Output?>
    open lateinit var fetchRemote: suspend (Input) -> Output?
    open lateinit var isFresh: (suspend (Input) -> Boolean)
    open var isEmptyResponse: (suspend (Output?) -> Boolean) = { it == null }
    open var onRefresh: (suspend () -> Unit)? = null
    open var localStore: (suspend (Output) -> Unit)? = null
    open var clearLocalStore: (suspend (Input) -> Unit)? = null

    protected open suspend fun getCache(args: Input) = fetchLocal(args)

    suspend fun query(args: Input, force: Boolean = false) = flow {
        if (force || !isFresh(args)) {
            try {
                fetchRemote(args).also {
                    if (isEmptyResponse(it))
                        clearLocalStore?.invoke(args)
                    else
                        localStore?.invoke(it!!)
                }
                onRefresh?.invoke()
            } catch (e: UnableToObtainResource) {}
        }
        emitAll(getCache(args))
    }
}
