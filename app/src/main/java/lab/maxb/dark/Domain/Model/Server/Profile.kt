package lab.maxb.dark.Domain.Model.Server

import lab.maxb.dark.Domain.Model.Role
import lab.maxb.dark.Domain.Model.User
import lab.maxb.dark.Domain.Operations.getUUID
import lab.maxb.dark.Domain.Operations.toSHA256


open class Profile(
    override var name: String,
    override var rating: Int,
    open var role: Role = Role.USER,
    override var id: String = getUUID(),
    open var hash: String = EMPTY_HASH,
    password: String? = null,
): User(name, rating, id) {
    init {
        password?.let { hash = getHash(name, it) }
    }

    companion object {
        const val EMPTY_HASH = ""

        fun getHash(name: String, password: String)
            = (name + password).toSHA256()
    }
}