package lab.maxb.dark.data.local.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import lab.maxb.dark.data.model.local.RecognitionTaskLocalDTO
import lab.maxb.dark.data.model.local.UserLocalDTO
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.domain.model.User

data class FullRecognitionTaskDTO(
    @Embedded val recognition_task: RecognitionTaskLocalDTO,
    @Relation(
        parentColumn = "owner_id",
        entityColumn = "id",
        entity = UserLocalDTO::class
    )
    val owner: User?
)

fun FullRecognitionTaskDTO.toDomain() = RecognitionTask(
    recognition_task.names,
    recognition_task.images,
    owner,
    recognition_task.reviewed,
    recognition_task.id,
)
