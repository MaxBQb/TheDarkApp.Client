package lab.maxb.dark.data.model.remote

import lab.maxb.dark.domain.model.RecognitionTask


class RecognitionTaskListViewNetworkDTO (
    val image: String?,
    val ownerId: String,
    val reviewed: Boolean,
    val id: String,
)

class RecognitionTaskFullViewNetworkDTO(
    val names: Set<String>?,
    val images: List<String>?,
    val ownerId: String,
    val reviewed: Boolean,
    val id: String,
)

class RecognitionTaskCreationNetworkDTO(
    val names: Set<String>,
    val images: List<String>,
)

fun RecognitionTask.toNetworkDTO() = RecognitionTaskCreationNetworkDTO(
    names,
    images,
)

fun RecognitionTaskListViewNetworkDTO.toDomain() = RecognitionTask(
    setOf(),
    image?.let { listOf(it) } ?: emptyList(),
    ownerId,
    reviewed,
    id,
)

fun RecognitionTaskFullViewNetworkDTO.toDomain() = RecognitionTask(
    names ?: emptySet(),
    images ?: emptyList(),
    ownerId,
    reviewed,
    id,
)