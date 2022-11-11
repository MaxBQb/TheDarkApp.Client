package lab.maxb.dark.domain.usecase.settings.locale

import kotlinx.coroutines.flow.first
import lab.maxb.dark.domain.repository.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class HandleCurrentLocaleUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    open suspend operator fun invoke(current: String, system: String)
        = settingsRepository.settings.first().let { settings ->
            settings.locale.ifEmpty {
                val locale = if (settings.systemLocale.isEmpty())
                    current.ifEmpty { system }
                    else system
                settingsRepository.change {
                    it.copy(systemLocale = locale)
                }
                locale
            }
        }
}