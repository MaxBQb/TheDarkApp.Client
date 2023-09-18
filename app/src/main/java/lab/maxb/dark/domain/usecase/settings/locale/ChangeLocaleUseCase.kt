package lab.maxb.dark.domain.usecase.settings.locale

import kotlinx.coroutines.flow.first
import lab.maxb.dark.domain.repository.SettingsRepository
import org.koin.core.annotation.Singleton

@Singleton
open class ChangeLocaleUseCase(
    private val settingsRepository: SettingsRepository,
) {
    open suspend operator fun invoke(locale: String): String {
        settingsRepository.update { it.copy(locale = locale) }
        return settingsRepository.settings.first().let {
            it.locale.ifEmpty { it.systemLocale }
        }
    }
}