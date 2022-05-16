package lab.maxb.dark.presentation.repository.utils

import kotlinx.coroutines.flow.firstOrNull

class Resource<Input, Output>(
    var refreshController: RefreshController = RefreshControllerImpl()
) : BaseResource<Input, Output>() {

    override var isFresh: suspend (Input) -> Boolean = {
        !refreshController.isExpired() && getCache(it).firstOrNull() != null
    }

    override var onRefresh: (suspend () -> Unit)? = {
        refreshController.refresh()
    }
}

