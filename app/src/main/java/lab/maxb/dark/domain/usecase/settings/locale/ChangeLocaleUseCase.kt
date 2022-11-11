package lab.maxb.dark.domain.usecase.settings.locale

import lab.maxb.dark.domain.repository.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class ChangeLocaleUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    open suspend operator fun invoke(locale: String) =
        settingsRepository.change { it.copy(locale = locale) }
            .let { it.locale.ifEmpty { it.systemLocale } }
}