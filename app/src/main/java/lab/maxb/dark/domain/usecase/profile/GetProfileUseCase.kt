package lab.maxb.dark.domain.usecase.profile

import lab.maxb.dark.domain.repository.ProfileRepository
import org.koin.core.annotation.Singleton

@Singleton
class GetProfileUseCase(
    private val profileRepository: ProfileRepository,
) {
    operator fun invoke() = profileRepository.profile
}