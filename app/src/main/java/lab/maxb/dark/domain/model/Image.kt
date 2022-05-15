package lab.maxb.dark.domain.model

import lab.maxb.dark.domain.operations.randomUUID

open class Image(
    open var path: String = "",
    open var id: String = randomUUID,
)