package lab.maxb.dark.Presentation.Room.Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import lab.maxb.dark.Domain.Model.RecognitionTask

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
    val owner_id: String,
    override var image: String?,
): RecognitionTask(image=image, id=id) {
    constructor(task: RecognitionTask) : this(
        task.id,
        task.owner!!.id,
        task.image,
    )
}