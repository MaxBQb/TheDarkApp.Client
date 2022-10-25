package lab.maxb.dark.data.local.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import lab.maxb.dark.data.model.local.RecognitionTaskLocalDTO
import lab.maxb.dark.data.model.local.UserLocalDTO
import lab.maxb.dark.data.model.local.toDomain
import lab.maxb.dark.domain.model.RecognitionTaskWithOwner
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

fun FullRecognitionTaskDTO.toDomain() = RecognitionTaskWithOwner(
    recognition_task.toDomain(),
    owner!!,
)
