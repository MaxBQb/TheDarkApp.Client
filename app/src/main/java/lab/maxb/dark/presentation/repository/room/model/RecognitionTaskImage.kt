package lab.maxb.dark.presentation.repository.room.model
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import lab.maxb.dark.domain.operations.randomUUID

@Entity(tableName = "recognition_task_image",
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
)
data class RecognitionTaskImage(
    val recognition_task: String,
    val image: String,
    @PrimaryKey
    val id: String = randomUUID,
)
