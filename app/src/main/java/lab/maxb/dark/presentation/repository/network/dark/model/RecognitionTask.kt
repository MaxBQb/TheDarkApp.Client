package lab.maxb.dark.presentation.repository.network.dark.model

import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.domain.model.User


class RecognitionTaskListViewNetworkDTO (
    var image: String?,
    val owner_id: String,
    val reviewed: Boolean,
    var id: String,
)

class RecognitionTaskFullViewNetworkDTO(
    var names: Set<String>?,
    var images: List<String>?,
    val owner_id: String,
    val reviewed: Boolean,
    var id: String,
)

class RecognitionTaskCreationNetworkDTO(
    var names: Set<String>
)

fun RecognitionTask.toNetworkDTO() = RecognitionTaskCreationNetworkDTO(
    names!!
)

inline fun RecognitionTaskListViewNetworkDTO.toDomain(
    user: () -> User? = { null },
) = RecognitionTask(
    setOf(),
    image?.let { listOf(it) },
    user(),
    reviewed,
    id,
)

inline fun RecognitionTaskFullViewNetworkDTO.toDomain(
    user: () -> User? = { null }
) = RecognitionTask(
    names,
    images,
    user(),
    reviewed,
    id,
)