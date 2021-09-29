package lab.maxb.dark.Domain.Operations

import android.graphics.Bitmap
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.Domain.Model.User

fun createRecognitionTask(
    names: List<String>,
    image: String,
    owner: User?
): RecognitionTask? {
    val namesSet = names.map { it.trim().lowercase() }
        .filter { it.isNotBlank() }
        .toSet()

    if (namesSet.isEmpty())
        return null

    return RecognitionTask(
        namesSet,
        image,
        owner ?: User("", 0, "UUID")
    )
}