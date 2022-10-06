package lab.maxb.dark.domain.usecase.auth

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lab.maxb.dark.domain.repository.LocalStorage
import lab.maxb.dark.domain.repository.UserSettings
import org.koin.core.annotation.Singleton

@Singleton
open class SignOutUseCase(
    private val settings: UserSettings,
    private val storage: LocalStorage,
) {
    suspend operator fun invoke() = withContext(Dispatchers.Default) {
        settings.clear()
        storage.clear()
    }
}
