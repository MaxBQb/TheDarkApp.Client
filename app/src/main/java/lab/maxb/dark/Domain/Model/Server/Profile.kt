package lab.maxb.dark.Domain.Model.Server

import lab.maxb.dark.Domain.Model.Role
import lab.maxb.dark.Domain.Model.User
import lab.maxb.dark.Domain.Operations.getUUID
import lab.maxb.dark.Domain.Operations.toSHA256


open class Profile(
    open var login: String,
    override var name: String,
    override var rating: Int,
    open var role: Role = Role.USER,
    override var id: String = getUUID(),
    open var hash: String? = null,
    password: String? = null,
): User(name, rating, id) {
    init {
        password?.let { hash = getHash(login, it) }
    }

    companion object {
        fun getHash(login: String, password: String)
            = (login + password).toSHA256()
    }
}