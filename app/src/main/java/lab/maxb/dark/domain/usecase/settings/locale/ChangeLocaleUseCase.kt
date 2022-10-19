package lab.maxb.dark.domain.usecase.settings.locale

import lab.maxb.dark.domain.repository.SettingsRepository
import org.koin.core.annotation.Singleton

@Singleton
open class ChangeLocaleUseCase(
    private val settingsRepository: SettingsRepository,
) {
    open suspend operator fun invoke(locale: String) =
        settingsRepository.change { it.copy(locale = locale) }
            .let { it.locale.ifEmpty { it.systemLocale } }
}