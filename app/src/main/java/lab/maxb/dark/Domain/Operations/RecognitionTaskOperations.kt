package lab.maxb.dark.Domain.Operations

import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.Domain.Model.User

fun createRecognitionTask(
    names: List<String>,
    images: List<String>,
    owner: User?
): RecognitionTask? {
    val namesSet = names.map { prepareRecognitionTaskName(it) }
        .filter { it.isNotBlank() }
        .toSet()

    if (namesSet.isEmpty() or images.isEmpty())
        return null

    return RecognitionTask(
        namesSet,
        images,
        owner ?: User("", 0, "UUID")
    )
}

fun RecognitionTask.solve(name: String)
    = names?.contains(prepareRecognitionTaskName(name)) ?: false

fun prepareRecognitionTaskName(name: String) = name.trim().lowercase()