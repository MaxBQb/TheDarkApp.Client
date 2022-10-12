package lab.maxb.dark.domain.usecase.auth

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lab.maxb.dark.domain.model.AuthCredentials
import lab.maxb.dark.domain.repository.LocalStorage
import lab.maxb.dark.domain.repository.ProfileRepository
import lab.maxb.dark.domain.repository.UserSettings
import org.koin.core.annotation.Singleton

@Singleton
open class SignOutUseCase(
    private val settings: UserSettings,
    private val storage: LocalStorage,
    private val profileRepository: ProfileRepository,
) {
    open suspend operator fun invoke() = withContext(Dispatchers.Default) {
        settings.clear()
        storage.clear()
        profileRepository.sendCredentials(AuthCredentials("")) // TODO: Observe settings.token instead
    }
}
