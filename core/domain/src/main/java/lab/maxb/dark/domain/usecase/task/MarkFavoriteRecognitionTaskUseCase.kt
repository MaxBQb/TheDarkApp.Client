package lab.maxb.dark.domain.usecase.task

import kotlinx.coroutines.flow.firstOrNull
import lab.maxb.dark.domain.repository.RecognitionTasksRepository
import org.koin.core.annotation.Singleton

@Singleton
open class MarkFavoriteRecognitionTaskUseCase(
    private val recognitionTasksRepository: RecognitionTasksRepository,
) {
    open suspend operator fun invoke(taskId: String, isFavorite: Boolean) {
        val task = recognitionTasksRepository.recognitionTaskResource.query(taskId, useCache = true).firstOrNull()!!
        recognitionTasksRepository.markFavoriteRecognitionTask(task.copy(
            favorite = isFavorite
        ))
    }
}