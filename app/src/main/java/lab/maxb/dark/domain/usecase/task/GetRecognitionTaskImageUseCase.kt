package lab.maxb.dark.domain.usecase.task

import lab.maxb.dark.domain.repository.RecognitionTasksRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class GetRecognitionTaskImageUseCase @Inject constructor(
    private val recognitionTasksRepository: RecognitionTasksRepository,
) {
    open operator fun invoke(path: String)
        = recognitionTasksRepository.getRecognitionTaskImage(path)
}