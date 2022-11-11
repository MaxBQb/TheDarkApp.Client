package lab.maxb.dark.domain.usecase.task

import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.domain.repository.RecognitionTasksRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class MarkRecognitionTaskUseCase @Inject constructor(
    private val recognitionTasksRepository: RecognitionTasksRepository,
) {
    open suspend operator fun invoke(task: RecognitionTask, isAllowed: Boolean) {
        recognitionTasksRepository.markRecognitionTask(task.copy(
            reviewed = isAllowed
        ))
    }
}