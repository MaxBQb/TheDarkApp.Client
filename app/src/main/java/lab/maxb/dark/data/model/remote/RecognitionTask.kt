package lab.maxb.dark.data.model.remote

import lab.maxb.dark.domain.model.RecognitionTask


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

fun RecognitionTaskListViewNetworkDTO.toDomain() = RecognitionTask(
    setOf(),
    image?.let { listOf(it) },
    owner_id,
    reviewed,
    id,
)

fun RecognitionTaskFullViewNetworkDTO.toDomain() = RecognitionTask(
    names,
    images,
    owner_id,
    reviewed,
    id,
)