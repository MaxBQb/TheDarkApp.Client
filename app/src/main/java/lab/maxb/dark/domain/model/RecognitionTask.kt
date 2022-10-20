package lab.maxb.dark.domain.model

import lab.maxb.dark.domain.operations.randomUUID

data class RecognitionTask(
    val names: Set<String>? = null,
    val images: List<String>? = null,
    val owner: User? = null,
    val reviewed: Boolean = false,
    val id: String = randomUUID,
) {
    companion object {
        const val MAX_IMAGES_COUNT = 6
    }
}