package lab.maxb.dark.presentation.repository.utils

import kotlinx.coroutines.flow.firstOrNull

open class StaticResource<Input, Output> : BaseResource<Input, Output>() {
    override var isFresh: suspend (Input) -> Boolean = {
        getCache(it).firstOrNull() != null
    }
}