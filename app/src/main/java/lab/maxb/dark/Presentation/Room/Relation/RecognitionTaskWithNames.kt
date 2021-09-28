package lab.maxb.dark.Presentation.Room.Relation
import androidx.room.Embedded
import androidx.room.Relation
import lab.maxb.dark.Presentation.Room.Model.RecognitionTaskDTO
import lab.maxb.dark.Presentation.Room.Model.RecognitionTaskName

data class RecognitionTaskWithNames(
    @Embedded val recognition_task: RecognitionTaskDTO,
    @Relation(
        parentColumn = "id",
        entityColumn = "recognition_task"
    )
    val names: List<RecognitionTaskName>
)
