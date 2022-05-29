package lab.maxb.dark.presentation.repository.room.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import lab.maxb.dark.domain.model.RecognitionTask

@Entity(tableName = "recognition_task",
        ignoredColumns = ["owner", "names"],
        foreignKeys = [
            ForeignKey(
                entity = UserDTO::class,
                parentColumns = ["id"],
                childColumns = ["owner_id"],
                onDelete = ForeignKey.CASCADE
            )
        ]
)
data class RecognitionTaskDTO(
    @PrimaryKey
    override var id: String,
    override var images: List<String>?,
    val owner_id: String,
    override var reviewed: Boolean = false,
): RecognitionTask(id=id) {
    constructor(task: RecognitionTask) : this(
        task.id,
        task.images,
        task.owner!!.id,
        task.reviewed,
    )
}