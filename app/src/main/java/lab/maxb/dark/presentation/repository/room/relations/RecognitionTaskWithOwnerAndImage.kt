package lab.maxb.dark.presentation.repository.room.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.domain.model.User
import lab.maxb.dark.presentation.repository.room.model.*

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
        entityColumn = "imageId",
        associateBy = Junction(RecognitionTaskImageCrossref::class)
    )
    val image: ImageDTO?
) {
    fun toRecognitionTask() = recognition_task.also { task ->
        task.owner = owner
        task.images = image?.let { listOf(it.toImage()) } ?: listOf()
    } as RecognitionTask
}
