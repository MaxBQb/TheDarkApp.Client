package lab.maxb.dark.data.model.remote

import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.domain.model.modelRefOf


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
    names = setOf(),
    images = image?.let { listOf(it) } ?: emptyList(),
    owner = modelRefOf(ownerId),
    reviewed = reviewed,
    favorite = null,
    id=id,
)

fun RecognitionTaskFullViewNetworkDTO.toDomain() = RecognitionTask(
    names = names ?: emptySet(),
    images = images ?: emptyList(),
    owner = modelRefOf(ownerId),
    reviewed = reviewed,
    favorite = null,
    id=id,
)