package lab.maxb.dark.presentation.repository.utils

import java.util.*
import java.util.concurrent.TimeUnit

class RefreshControllerImpl(
    private val refreshRate: Long = DEFAULT_REFRESH_RATE,
    private var lastUpdate: Date? = null
) : RefreshController {
    companion object {
        val DEFAULT_REFRESH_RATE = TimeUnit.MINUTES.toMillis(5)
    }

    override suspend fun isExpired() = lastUpdate?.let {
        (Date().time - it.time) > refreshRate
    } ?: true

    override suspend fun refresh() {
        lastUpdate = Date()
    }
}
