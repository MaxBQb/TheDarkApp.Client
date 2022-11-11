package lab.maxb.dark.domain.usecase.settings.locale

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import lab.maxb.dark.domain.repository.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class GetCurrentLocaleUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    open operator fun invoke()
        = settingsRepository.settings.mapLatest { it.locale }
}