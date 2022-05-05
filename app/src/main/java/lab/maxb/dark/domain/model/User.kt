package lab.maxb.dark.domain.model

import lab.maxb.dark.domain.operations.randomUUID

open class User(
    open var name: String,
    open var rating: Int,
    open var id: String = randomUUID,
)