package lab.maxb.dark.presentation.repository.room.model
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

@Entity(tableName = "recognition_task_image",
    primaryKeys = ["id", "imageId"],
    foreignKeys = [
            ForeignKey(
                entity = RecognitionTaskDTO::class,
                parentColumns = ["id"],
                childColumns = ["id"],
                onDelete = CASCADE,
                onUpdate = CASCADE,
                deferred = true
            ),
            ForeignKey(
                entity = ImageDTO::class,
                parentColumns = ["imageId"],
                childColumns = ["imageId"],
                onDelete = CASCADE,
                deferred = true
            )
        ],
)
data class RecognitionTaskImageCrossref(
    var id: String,
    var imageId: String,
)
