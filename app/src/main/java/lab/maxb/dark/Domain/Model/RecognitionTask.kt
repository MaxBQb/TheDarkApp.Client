package lab.maxb.dark.Domain.Model

import lab.maxb.dark.Domain.Operations.randomUUID

open class RecognitionTask(
    open var names: Set<String>? = null,
    open var images: List<String>? = null,
    open var owner: User? = null,
    open var reviewed: Boolean = false,
    open var id: String = randomUUID,
) {
    companion object {
        const val MAX_IMAGES_COUNT = 6
    }
}