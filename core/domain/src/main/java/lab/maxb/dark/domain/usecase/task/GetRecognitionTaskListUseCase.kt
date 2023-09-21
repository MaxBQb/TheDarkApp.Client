package lab.maxb.dark.domain.usecase.task

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import lab.maxb.dark.domain.model.LayerSpecific
import lab.maxb.dark.domain.model.Pageable
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.domain.model.RecognitionTaskComplete
import lab.maxb.dark.domain.repository.ProfileRepository
import lab.maxb.dark.domain.repository.RecognitionTasksRepository
import org.koin.core.annotation.Singleton

private typealias LayerSpecificApply<T, F> = (LayerSpecific<T>, F) -> LayerSpecific<T>
private typealias LayerSpecificFilter<T, T1> = LayerSpecificApply<T, (T1) -> Boolean>

@Singleton
open class GetRecognitionTaskListUseCase(
    private val recognitionTasksRepository: RecognitionTasksRepository,
    private val profileRepository: ProfileRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    open operator fun invoke(
        filter: LayerSpecificFilter<Pageable<RecognitionTaskComplete>, RecognitionTask>
    )
        = profileRepository.profile.flatMapLatest { profile ->
            recognitionTasksRepository.getAllRecognitionTasks().mapLatest { page ->
                filter(page) {
                    it.owner.id != profile?.userId
                }
            }
        }
}