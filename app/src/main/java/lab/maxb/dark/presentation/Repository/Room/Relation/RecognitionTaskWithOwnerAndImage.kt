package lab.maxb.dark.presentation.Repository.Room.Relation

import androidx.room.Embedded
import androidx.room.Relation
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.Domain.Model.User
import lab.maxb.dark.presentation.Repository.Room.Model.RecognitionTaskDTO
import lab.maxb.dark.presentation.Repository.Room.Model.RecognitionTaskImage
import lab.maxb.dark.presentation.Repository.Room.Model.UserDTO

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
        task.images = image?.image?.let{ listOf(it) } ?: listOf()
    } as RecognitionTask
}
