package lab.maxb.dark.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import lab.maxb.dark.domain.repository.SettingsRepository
import org.koin.core.annotation.Singleton

@Singleton
open class GetCurrentLocaleUseCase(
    private val settingsRepository: SettingsRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    open operator fun invoke()
        = settingsRepository.settings.mapLatest { it.locale }
}