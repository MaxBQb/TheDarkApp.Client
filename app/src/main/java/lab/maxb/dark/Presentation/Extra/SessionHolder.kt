package lab.maxb.dark.Presentation.Extra

import android.content.Context
import lab.maxb.dark.Domain.Model.Server.Session
import lab.maxb.dark.Presentation.Extra.Delegates.shared

class SessionHolder(context: Context) {
    var sessionId by context shared SESSION_INFO
    var sessionHash by context shared SESSION_INFO

    fun saveSession(session: Session) = with (session) {
        sessionId = id
        sessionHash = hash
    }

    companion object {
        val SESSION_INFO: String = ::SESSION_INFO.name
    }
}