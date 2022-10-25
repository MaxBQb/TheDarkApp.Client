package lab.maxb.dark.domain.operations

import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.domain.model.User

fun createRecognitionTask(
    names: List<String>,
    images: List<String>,
    owner: User
): RecognitionTask? {
    val namesSet = names.map { prepareRecognitionTaskName(it) }
        .filter { it.isNotBlank() }
        .toSet()

    if (namesSet.isEmpty() or images.isEmpty())
        return null

    return RecognitionTask(
        namesSet,
        images,
        owner.id,
    )
}

fun RecognitionTask.solve(name: String)
    = names?.contains(prepareRecognitionTaskName(name)) ?: false

fun prepareRecognitionTaskName(name: String)
    = name.trim().lowercase()


const val shareLink = "https://the.dark.app/"