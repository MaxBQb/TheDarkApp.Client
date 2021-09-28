package lab.maxb.dark.Presentation.Room.Model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.Domain.Model.User

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
    override val image: Bitmap?,
): RecognitionTask(image=image, id=id) {
    override var names: Set<String>? = null
    override var owner: User? = null

    constructor(task: RecognitionTask) : this(
        task.id,
        task.owner!!.id,
        task.image,
    )
}