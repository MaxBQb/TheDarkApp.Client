package lab.maxb.dark.domain.repository

interface UserSettings {
    var token: String
    var login: String

    fun clear()
}
