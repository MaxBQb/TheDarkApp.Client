package lab.maxb.dark.Domain.Model

import java.util.*

open class User(
    open var name: String,
    open var rating: Int,
    open val id: String = UUID.randomUUID().toString(),
)