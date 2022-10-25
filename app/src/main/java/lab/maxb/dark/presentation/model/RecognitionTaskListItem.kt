package lab.maxb.dark.presentation.model

import lab.maxb.dark.domain.model.RecognitionTaskWithOwner

data class RecognitionTaskListItem(
    val image: String,
    val ownerName: String,
    val reviewed: Boolean = false,
    val id: String,
)

fun RecognitionTaskWithOwner.toPresentation() = RecognitionTaskListItem(
    image = task.images.firstOrNull() ?: "",
    ownerName = owner.name,
    reviewed = task.reviewed,
    id = task.id,
)