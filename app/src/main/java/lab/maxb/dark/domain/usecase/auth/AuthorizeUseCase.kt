package lab.maxb.dark.domain.usecase.auth

import kotlinx.coroutines.flow.first
import lab.maxb.dark.domain.model.AuthCredentials
import lab.maxb.dark.domain.repository.ProfileRepository
import org.koin.core.annotation.Singleton

@Singleton
class AuthorizeUseCase(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(credentials: AuthCredentials)
        = if (profileRepository.sendCredentials(credentials))
            profileRepository.profile.first()
        else null
}
