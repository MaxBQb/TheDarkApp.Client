package lab.maxb.dark.domain.model

import lab.maxb.dark.domain.operations.randomUUID


data class RecognitionTaskBase<out OWNER_REF_TYPE : ModelRef<User>>(
    val names: Set<String> = emptySet(),
    val images: List<String> = emptyList(),
    val owner: OWNER_REF_TYPE,
    val reviewed: Boolean = false,
    val favorite: Boolean? = false,
    override val id: String = randomUUID,
): BaseModel {
    companion object {
        const val MAX_IMAGES_COUNT = 6
    }
}

typealias RecognitionTask = RecognitionTaskBase<ModelRef<User>>  // Default template
// Compiler guarantees that owner is presented
typealias RecognitionTaskComplete = RecognitionTaskBase<ModelRef.Ref<User>>
