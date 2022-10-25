package lab.maxb.dark.domain.model

data class RecognitionTaskWithOwner(
    val task: RecognitionTask,
    val owner: User,
)