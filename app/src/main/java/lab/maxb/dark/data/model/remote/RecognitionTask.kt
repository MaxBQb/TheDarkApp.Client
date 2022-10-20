package lab.maxb.dark.data.model.remote

import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.domain.model.User


class RecognitionTaskListViewNetworkDTO (
    val image: String?,
    val owner_id: String,
    val reviewed: Boolean,
    val id: String,
)

class RecognitionTaskFullViewNetworkDTO(
    val names: Set<String>?,
    val images: List<String>?,
    val owner_id: String,
    val reviewed: Boolean,
    val id: String,
)

class RecognitionTaskCreationNetworkDTO(
    val names: Set<String>
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