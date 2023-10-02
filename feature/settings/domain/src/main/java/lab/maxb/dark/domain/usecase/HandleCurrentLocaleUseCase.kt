package lab.maxb.dark.domain.usecase

import kotlinx.coroutines.flow.first
import lab.maxb.dark.domain.repository.SettingsRepository
import org.koin.core.annotation.Singleton

@Singleton
open class HandleCurrentLocaleUseCase(
    private val settingsRepository: SettingsRepository,
) {
    open suspend operator fun invoke(current: String, system: String)
        = settingsRepository.settings.first().let { settings ->
            settings.locale.ifEmpty {
                val locale = if (settings.systemLocale.isEmpty())
                    current.ifEmpty { system }
                    else system
                settingsRepository.update {
                    it.copy(systemLocale = locale)
                }
                locale
            }
        }
}