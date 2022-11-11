package lab.maxb.dark.domain.usecase.profile

import lab.maxb.dark.domain.repository.ProfileRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class RefreshProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    open suspend operator fun invoke() = profileRepository.profileResource.retry()
}