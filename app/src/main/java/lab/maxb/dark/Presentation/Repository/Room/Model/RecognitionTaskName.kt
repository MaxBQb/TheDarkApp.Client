package lab.maxb.dark.Presentation.Repository.Room.Model
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

@Entity(tableName = "recognition_task_name",
        foreignKeys = [
            ForeignKey(
                entity = RecognitionTaskDTO::class,
                parentColumns = ["id"],
                childColumns = ["recognition_task"],
                onDelete = CASCADE,
                onUpdate = CASCADE,

                deferred = true
            )
        ],
    primaryKeys = ["recognition_task", "name"]
)
data class RecognitionTaskName(
    val recognition_task: String,
    val name: String,
)
