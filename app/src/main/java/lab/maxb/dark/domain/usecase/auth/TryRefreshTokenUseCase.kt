package lab.maxb.dark.domain.usecase.auth

import org.koin.core.annotation.Singleton

@Singleton
open class TryRefreshTokenUseCase(
    private val signOutUseCase: SignOutUseCase,
) {
    suspend operator fun invoke(): Boolean {
        signOutUseCase() // TODO: Use refresh token
        return false
    }
}
