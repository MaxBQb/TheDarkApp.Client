package lab.maxb.dark.ui.model

import lab.maxb.dark.domain.model.RecognitionTaskComplete

data class RecognitionTaskListItem(
    val image: String,
    val ownerName: String,
    val reviewed: Boolean = false,
    val favorite: Boolean = false,
    val id: String,
)

fun RecognitionTaskComplete.toPresentation() = RecognitionTaskListItem(
    image = images.firstOrNull() ?: "",
    ownerName = owner.value.name,
    reviewed = reviewed,
    favorite = favorite ?: false,
    id = id,
)