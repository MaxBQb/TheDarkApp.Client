package lab.maxb.dark.domain.usecase.profile

import lab.maxb.dark.domain.repository.ProfileRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class GetProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    open operator fun invoke() = profileRepository.profile
}