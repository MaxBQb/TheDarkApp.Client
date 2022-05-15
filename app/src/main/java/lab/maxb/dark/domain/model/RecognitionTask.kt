package lab.maxb.dark.domain.model

import lab.maxb.dark.domain.operations.randomUUID

open class RecognitionTask(
    open var names: Set<String>? = null,
    open var images: List<Image>? = null,
    open var owner: User? = null,
    open var reviewed: Boolean = false,
    open var id: String = randomUUID,
) {
    companion object {
        const val MAX_IMAGES_COUNT = 6
    }
}