package lab.maxb.dark.data.utils

import lab.maxb.dark.data.model.local.TimeContainer
import java.util.concurrent.TimeUnit

class DbRefreshController<T: TimeContainer>(
    private val refreshRate: Long = DEFAULT_REFRESH_RATE,
) : RefreshController<T> {

    companion object {
        val DEFAULT_REFRESH_RATE = TimeUnit.MINUTES.toMillis(5)
    }

    override suspend fun isExpired(data: T?) = data?.let {
        (System.currentTimeMillis() - it.modifiedAt) > refreshRate
    } ?: true

    override suspend fun refresh() { }
}
