package lab.maxb.dark.Presentation.Extra

import android.content.Context
import lab.maxb.dark.Presentation.Extra.Delegates.shared

class SessionHolder(context: Context) {
    var token by context shared SESSION_INFO
    var login by context shared SESSION_INFO

    companion object {
        val SESSION_INFO: String = ::SESSION_INFO.name
    }
}