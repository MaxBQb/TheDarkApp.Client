package lab.maxb.dark.domain.usecase.task

import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.domain.repository.RecognitionTasksRepository
import org.koin.core.annotation.Singleton

@Singleton
open class MarkRecognitionTaskUseCase(
    private val recognitionTasksRepository: RecognitionTasksRepository,
) {
    open suspend operator fun invoke(task: RecognitionTask, isAllowed: Boolean) {
        task.reviewed = isAllowed
        recognitionTasksRepository.markRecognitionTask(task)
    }
}