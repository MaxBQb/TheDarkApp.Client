package lab.maxb.dark.Presentation.Extra

import android.content.Context
import lab.maxb.dark.Domain.Model.Server.Session
import lab.maxb.dark.Presentation.Extra.Delegates.shared
import kotlin.properties.Delegates

class SessionHolder(context: Context) {
    var sessionId by context shared SESSION_INFO
        private set

    var sessionHash by context shared SESSION_INFO
        private set

    var session: Session? by Delegates.observable(null) {
        _, _, value ->
        sessionId = value?.id
        sessionHash = value?.hash
    }

    companion object {
        val SESSION_INFO: String = ::SESSION_INFO.name
    }
}