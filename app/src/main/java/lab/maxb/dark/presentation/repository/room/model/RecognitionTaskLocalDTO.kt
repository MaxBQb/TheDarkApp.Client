package lab.maxb.dark.presentation.repository.room.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.domain.operations.randomUUID

@Entity(tableName = "recognition_task",
        foreignKeys = [
            ForeignKey(
                entity = UserLocalDTO::class,
                parentColumns = ["id"],
                childColumns = ["owner_id"],
                onDelete = ForeignKey.CASCADE
            )
        ]
)
data class RecognitionTaskLocalDTO(
    var names: Set<String>?,
    var images: List<String>?,
    val owner_id: String,
    var reviewed: Boolean = false,

    @PrimaryKey
    override var id: String = randomUUID,
): BaseLocalDTO()

fun RecognitionTask.toLocalDTO() = RecognitionTaskLocalDTO(
    names,
    images,
    owner!!.id,
    reviewed,
    id,
)

fun RecognitionTaskLocalDTO.toDomain() = RecognitionTask(
    names,
    images,
    null,
    reviewed,
    id,
)