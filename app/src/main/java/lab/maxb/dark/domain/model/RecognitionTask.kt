package lab.maxb.dark.domain.model

import lab.maxb.dark.domain.operations.randomUUID

data class RecognitionTask(
    val names: Set<String> = emptySet(),
    val images: List<String> = emptyList(),
    val ownerId: String,
    val reviewed: Boolean = false,
    val favorite: Boolean? = false,
    val id: String = randomUUID,
) {
    companion object {
        const val MAX_IMAGES_COUNT = 6
    }
}