package lab.maxb.dark.Presentation.Repository.Room.Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import lab.maxb.dark.Domain.Model.RecognitionTask

@Entity(tableName = "recognition_task",
        ignoredColumns = ["owner", "names", "images"],
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
    val owner_id: String,
    override var reviewed: Boolean = false,
): RecognitionTask(id=id) {
    constructor(task: RecognitionTask) : this(
        task.id,
        task.owner!!.id,
        task.reviewed,
    )
}