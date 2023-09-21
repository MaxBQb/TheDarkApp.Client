package lab.maxb.dark.domain.usecase.auth

import lab.maxb.dark.domain.model.AuthCredentials
import lab.maxb.dark.domain.repository.ProfileRepository
import org.koin.core.annotation.Singleton

@Singleton
open class AuthorizeUseCase(
    private val profileRepository: ProfileRepository,
) {
    open suspend operator fun invoke(credentials: AuthCredentials)
        = profileRepository.sendCredentials(credentials)
}
