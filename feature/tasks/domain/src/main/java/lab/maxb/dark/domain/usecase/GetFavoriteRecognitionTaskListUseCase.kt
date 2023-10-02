package lab.maxb.dark.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import lab.maxb.dark.domain.model.isUser
import lab.maxb.dark.domain.repository.ProfileRepository
import lab.maxb.dark.domain.repository.RecognitionTasksRepository
import org.koin.core.annotation.Singleton

@Singleton
open class GetFavoriteRecognitionTaskListUseCase(
    private val recognitionTasksRepository: RecognitionTasksRepository,
    private val profileRepository: ProfileRepository,
) {
    open fun getPaged()
        = recognitionTasksRepository.getFavoriteRecognitionTasks()

    @OptIn(ExperimentalCoroutinesApi::class)
    open fun canBeObtained() = profileRepository.profile.flatMapLatest {
        if (it?.role?.isUser == true)
            recognitionTasksRepository.hasFavoriteRecognitionTasks()
        else
            flowOf(false)
    }
}