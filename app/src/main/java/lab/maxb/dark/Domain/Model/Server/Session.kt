package lab.maxb.dark.Domain.Model.Server

import lab.maxb.dark.Domain.Operations.getTimestampNow
import lab.maxb.dark.Domain.Operations.getUUID
import lab.maxb.dark.Domain.Operations.toSHA256

open class Session(
    open var profile: Profile?,
    open var expires: Long = getTimestampNow() + SESSION_MAX_DURATION,
    open var hash: String = EMPTY_HASH,
    open var id: String = getUUID(),
) {
    init {
        profile?.hash?.let { generateHash(it) }
    }

    private fun generateHash(passwordHash: String)  {
        hash = (id + expires + passwordHash).toSHA256()
    }

    fun prolong(passwordHash: String) {
        expires += SESSION_MAX_DURATION
        generateHash(passwordHash)
    }

    companion object {
        const val SESSION_MAX_DURATION = 10*24*60*60*1000
        const val EMPTY_HASH = ""
    }
}
