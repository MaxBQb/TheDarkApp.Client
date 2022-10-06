package lab.maxb.dark.domain.usecase.auth

import kotlinx.coroutines.flow.combine
import lab.maxb.dark.domain.repository.ProfileRepository
import org.koin.core.annotation.Singleton

@Singleton
open class CheckAuthStateUseCase(
    private val profileRepository: ProfileRepository,
    private val tryRefreshTokenUseCase: TryRefreshTokenUseCase,
) {
    private val flow by lazy {
        combine(profileRepository.isTokenExpired, profileRepository.profile) {
                tokenExpired, profile ->
            if (tokenExpired)
                tryRefreshTokenUseCase()
            else
                profile != null
        }
    }
    operator fun invoke() = flow
}
