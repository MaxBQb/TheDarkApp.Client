package lab.maxb.dark.Domain.Model

import lab.maxb.dark.Domain.Operations.getUUID

open class User(
    open var name: String,
    open var rating: Int,
    open var id: String = getUUID(),
)