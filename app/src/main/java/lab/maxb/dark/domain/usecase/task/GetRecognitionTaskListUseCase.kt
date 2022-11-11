package lab.maxb.dark.domain.usecase.task

import androidx.paging.filter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import lab.maxb.dark.domain.repository.ProfileRepository
import lab.maxb.dark.domain.repository.RecognitionTasksRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class GetRecognitionTaskListUseCase @Inject constructor(
    private val recognitionTasksRepository: RecognitionTasksRepository,
    private val profileRepository: ProfileRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    open operator fun invoke()
        = profileRepository.profile.flatMapLatest { profile ->
            recognitionTasksRepository.getAllRecognitionTasks().mapLatest { page ->
                page.filter {
                    it.owner.id != profile?.userId
                }
            }
        }
}