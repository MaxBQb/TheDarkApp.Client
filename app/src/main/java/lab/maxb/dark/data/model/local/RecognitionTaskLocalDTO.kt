package lab.maxb.dark.data.model.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.domain.model.modelRefOf
import lab.maxb.dark.domain.operations.randomUUID

@Entity(
    tableName = "recognition_task",
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
    var names: Set<String>,
    var images: List<String>,
    val owner_id: String,
    val reviewed: Boolean = false,
    val favorite: Boolean? = false,

    @PrimaryKey
    override var id: String = randomUUID,

    @Embedded
    val time: TimeContainerImpl = TimeContainerImpl(),
) : BaseLocalDTO(), TimeContainer by time

fun RecognitionTask.toLocalDTO() = RecognitionTaskLocalDTO(
    names = names,
    images = images,
    owner_id = owner.id,
    reviewed = reviewed,
    favorite = favorite,
    id = id,
)

fun RecognitionTaskLocalDTO.toDomain() = RecognitionTask(
    names = names,
    images = images,
    owner = modelRefOf(owner_id),
    reviewed = reviewed,
    favorite = favorite,
    id = id,
)