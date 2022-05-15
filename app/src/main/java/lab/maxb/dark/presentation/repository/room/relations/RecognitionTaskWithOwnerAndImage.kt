package lab.maxb.dark.presentation.repository.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.domain.model.User
import lab.maxb.dark.presentation.repository.room.model.RecognitionTaskDTO
import lab.maxb.dark.presentation.repository.room.model.RecognitionTaskImage
import lab.maxb.dark.presentation.repository.room.model.UserDTO
import lab.maxb.dark.presentation.repository.room.model.toImage

data class RecognitionTaskWithOwnerAndImage(
    @Embedded val recognition_task: RecognitionTaskDTO,
    @Relation(
        parentColumn = "owner_id",
        entityColumn = "id",
        entity = UserDTO::class
    )
    val owner: User?,
    @Relation(
        parentColumn = "id",
        entityColumn = "recognition_task"
    )
    val image: RecognitionTaskImage?
) {
    fun toRecognitionTask() = recognition_task.also { task ->
        task.owner = owner
        task.images = image?.let { listOf(it.toImage()) } ?: listOf()
    } as RecognitionTask
}
