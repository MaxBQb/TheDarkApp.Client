package lab.maxb.dark.domain.operations

import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.domain.model.RecognitionTaskComplete
import lab.maxb.dark.domain.model.User
import lab.maxb.dark.domain.model.modelRefOf

fun createRecognitionTask(
    names: List<String>,
    images: List<String>,
    owner: User
): RecognitionTaskComplete? {
    val namesSet = names.map { prepareRecognitionTaskName(it) }
        .filter { it.isNotBlank() }
        .toSet()

    if (namesSet.isEmpty() or images.isEmpty())
        return null

    return RecognitionTaskComplete(
        namesSet,
        images,
        modelRefOf(owner),
    )
}

fun RecognitionTask.solve(name: String)
    = names.contains(prepareRecognitionTaskName(name))

fun prepareRecognitionTaskName(name: String)
    = name.trim().lowercase()


const val shareLink = "https://the.dark.app/"