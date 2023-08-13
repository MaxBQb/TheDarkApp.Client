package lab.maxb.dark.domain.model

import lab.maxb.dark.domain.operations.randomUUID

data class RecognitionTask(
    val names: Set<String> = emptySet(),
    val images: List<String> = emptyList(),
    val owner: ModelRef<User>,
    val reviewed: Boolean = false,
    val favorite: Boolean? = false,
    override val id: String = randomUUID,
): BaseModel {
    companion object {
        const val MAX_IMAGES_COUNT = 6
    }
}
