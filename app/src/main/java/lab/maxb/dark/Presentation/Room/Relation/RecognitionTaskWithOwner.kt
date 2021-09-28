package lab.maxb.dark.Presentation.Room.Relation

import androidx.room.Embedded
import androidx.room.Relation
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.Domain.Model.User
import lab.maxb.dark.Presentation.Room.Model.RecognitionTaskDTO
import lab.maxb.dark.Presentation.Room.Model.UserDTO

data class RecognitionTaskWithOwner(
    @Embedded val recognition_task: RecognitionTaskDTO,
    @Relation(
        parentColumn = "owner_id",
        entityColumn = "id",
        entity = UserDTO::class
    )
    val owner: User?
) {
    fun toRecognitionTask() = recognition_task.also {
        it.owner = owner
    } as RecognitionTask
}
