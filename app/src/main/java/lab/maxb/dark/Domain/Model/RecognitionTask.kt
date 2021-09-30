package lab.maxb.dark.Domain.Model

import lab.maxb.dark.Domain.Operations.getUUID

open class RecognitionTask(
    open var names: Set<String>? = null,
    open var image: String? = null,
    open var owner: User? = null,
    open var id: String = getUUID(),
)