package lab.maxb.dark.domain.usecase

import lab.maxb.dark.domain.repository.ProfileRepository
import org.koin.core.annotation.Singleton

@Singleton
open class RefreshProfileUseCase(
    private val profileRepository: ProfileRepository,
) {
    open suspend operator fun invoke() = profileRepository.profileResource.retry()
}