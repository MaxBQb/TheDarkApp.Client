package lab.maxb.dark.domain.usecase

import lab.maxb.dark.domain.repository.RecognitionTasksRepository
import org.koin.core.annotation.Singleton

@Singleton
open class GetRecognitionTaskImageUseCase(
    private val recognitionTasksRepository: RecognitionTasksRepository,
) {
    open operator fun invoke(path: String)
        = recognitionTasksRepository.getRecognitionTaskImage(path)
}