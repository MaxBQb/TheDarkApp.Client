package lab.maxb.dark.domain.usecase.task

import lab.maxb.dark.domain.repository.RecognitionTasksRepository
import org.koin.core.annotation.Singleton

@Singleton
open class GetRecognitionTaskUseCase(
    private val recognitionTasksRepository: RecognitionTasksRepository,
) {
    open suspend operator fun invoke(id: String)
        = recognitionTasksRepository.getRecognitionTask(id)
}