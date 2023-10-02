package lab.maxb.dark.domain.usecase

import kotlinx.coroutines.flow.firstOrNull
import lab.maxb.dark.domain.operation.createRecognitionTask
import lab.maxb.dark.domain.repository.RecognitionTasksRepository
import org.koin.core.annotation.Singleton

@Singleton
open class CreateRecognitionTaskUseCase(
    private val recognitionTasksRepository: RecognitionTasksRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) {
    open suspend operator fun invoke(
        names: List<String>,
        images: List<String>,
    ) {
        val task = createRecognitionTask(
            names.filter { it.isNotBlank() },
            images,
            getCurrentUserUseCase().firstOrNull()!!
        )!!
        recognitionTasksRepository.addRecognitionTask(task)
    }
}