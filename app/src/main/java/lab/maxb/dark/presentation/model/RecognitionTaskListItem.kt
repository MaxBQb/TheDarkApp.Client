package lab.maxb.dark.presentation.model

import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.domain.model.requireValue

data class RecognitionTaskListItem(
    val image: String,
    val ownerName: String,
    val reviewed: Boolean = false,
    val favorite: Boolean = false,
    val id: String,
)

fun RecognitionTask.toPresentation() = RecognitionTaskListItem(
    image = images.firstOrNull() ?: "",
    ownerName = owner.requireValue().name,
    reviewed = reviewed,
    favorite = favorite ?: false,
    id = id,
)