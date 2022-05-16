package lab.maxb.dark.presentation.repository.room.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import lab.maxb.dark.domain.model.Image
import lab.maxb.dark.domain.operations.randomUUID

@Entity(tableName = "image")
data class ImageDTO(
    val path: String,
    @PrimaryKey
    val imageId: String = randomUUID,
)

fun ImageDTO.toImage() = Image(
    path, imageId
)

fun Image.toImageDTO() = ImageDTO(
    path, id
)

