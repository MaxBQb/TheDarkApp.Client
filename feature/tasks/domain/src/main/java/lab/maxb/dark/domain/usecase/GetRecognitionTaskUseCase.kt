package lab.maxb.dark.domain.usecase

import lab.maxb.dark.domain.repository.RecognitionTasksRepository
import org.koin.core.annotation.Singleton

@Singleton
open class GetRecognitionTaskUseCase(
    private val recognitionTasksRepository: RecognitionTasksRepository,
) {
    open suspend operator fun invoke(id: String) =
        recognitionTasksRepository.recognitionTaskResource.query(
            id,
            force = true,
            useCache = true
        )
}