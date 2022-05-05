package lab.maxb.dark.presentation.repository.room.relations
import androidx.room.Embedded
import androidx.room.Relation
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.presentation.repository.room.model.RecognitionTaskDTO
import lab.maxb.dark.presentation.repository.room.model.RecognitionTaskImage
import lab.maxb.dark.presentation.repository.room.model.RecognitionTaskName

data class RecognitionTaskWithNamesAndImages(
    @Embedded val recognition_task: RecognitionTaskDTO,
    @Relation(
        parentColumn = "id",
        entityColumn = "recognition_task"
    )
    val names: List<RecognitionTaskName>,
    @Relation(
        parentColumn = "id",
        entityColumn = "recognition_task",
    )
    val images: List<RecognitionTaskImage>
) {
    fun toRecognitionTask() = recognition_task.also { task ->
        task.names = names.map {
            it.name
        }.toSet()
        task.images = images.map {
            it.image
        }
    } as RecognitionTask
}
