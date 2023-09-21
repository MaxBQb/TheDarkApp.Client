package lab.maxb.dark.data.utils

import java.util.concurrent.TimeUnit

class InMemRefreshController<T>(
    private val refreshRate: Long = DEFAULT_REFRESH_RATE,
    private var lastUpdate: Long = 0
) : RefreshController<T> {

    companion object {
        val DEFAULT_REFRESH_RATE = TimeUnit.MINUTES.toMillis(5)
    }

    override suspend fun isExpired(data: T?)
        = (System.currentTimeMillis() - lastUpdate) > refreshRate

    override suspend fun refresh() {
        lastUpdate = System.currentTimeMillis()
    }
}
