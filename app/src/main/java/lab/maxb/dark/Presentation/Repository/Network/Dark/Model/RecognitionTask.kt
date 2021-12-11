package lab.maxb.dark_api.Model.POJO

class RecognitionTaskListViewDTO (
    var image: String?,
    val owner_id: String,
    val reviewed: Boolean,
    var id: String,
)

class RecognitionTaskFullViewDTO(
    var names: Set<String>?,
    var images: List<String>?,
    val owner_id: String,
    val reviewed: Boolean,
    var id: String,
)

class RecognitionTaskCreationDTO(
    var names: Set<String>? = null,
    var images: List<String>? = null,
    var owner_id: String,
)