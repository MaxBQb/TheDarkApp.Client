package lab.maxb.dark.domain.usecase

import lab.maxb.dark.domain.repository.ProfileRepository
import org.koin.core.annotation.Singleton

@Singleton
open class GetProfileUseCase(
    private val profileRepository: ProfileRepository,
) {
    open operator fun invoke() = profileRepository.profile
}