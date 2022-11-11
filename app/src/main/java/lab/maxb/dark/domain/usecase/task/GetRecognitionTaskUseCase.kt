package lab.maxb.dark.domain.usecase.task

import lab.maxb.dark.domain.repository.RecognitionTasksRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class GetRecognitionTaskUseCase @Inject constructor(
    private val recognitionTasksRepository: RecognitionTasksRepository,
) {
    open suspend operator fun invoke(id: String) =
        recognitionTasksRepository.recognitionTaskResource.query(
            id,
            force = true,
            useCache = true
        )
}