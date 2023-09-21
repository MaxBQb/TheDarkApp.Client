package lab.maxb.dark.domain.usecase.auth

import kotlinx.coroutines.flow.combine
import lab.maxb.dark.domain.repository.ProfileRepository
import org.koin.core.annotation.Singleton

@Singleton
open class HandleInitialAuthUseCase(
    private val profileRepository: ProfileRepository,
    private val signOutUseCase: SignOutUseCase,
) {
    protected open val flow by lazy {
        combine(
            profileRepository.profile,
            profileRepository.isTokenExpired,
        ) { profile, tokenExpired ->
            val authPossible = if (tokenExpired)
                tryRefreshToken()
            else
                !profile?.token.isNullOrEmpty()
            if (authPossible)
                if (profile == null)
                    throw ProfileNotFound()
                else true
            else false
        }
    }

    open operator fun invoke() = flow

    protected open suspend fun tryRefreshToken(): Boolean {
        signOutUseCase() // TODO: Use refresh token
        return false
    }
}

class ProfileNotFound : Exception()