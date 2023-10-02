package lab.maxb.dark.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import lab.maxb.dark.data.local.model.RecognitionTaskLocalDTO
import lab.maxb.dark.data.local.model.UserLocalDTO
import lab.maxb.dark.domain.model.RecognitionTaskComplete
import lab.maxb.dark.domain.model.User
import lab.maxb.dark.domain.model.modelRefOf

data class FullRecognitionTaskDTO(
    @Embedded val recognition_task: RecognitionTaskLocalDTO,
    @Relation(
        parentColumn = "owner_id",
        entityColumn = "id",
        entity = UserLocalDTO::class
    )
    val owner: User
)

fun FullRecognitionTaskDTO.toDomain() = RecognitionTaskComplete(
    names = recognition_task.names,
    images = recognition_task.images,
    owner = modelRefOf(owner),
    reviewed = recognition_task.reviewed,
    favorite = recognition_task.favorite,
    id = recognition_task.id,
)
