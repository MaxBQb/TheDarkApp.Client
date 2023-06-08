package lab.maxb.dark.domain.usecase.settings

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import lab.maxb.dark.domain.repository.SettingsRepository
import org.koin.core.annotation.Singleton

@Singleton
open class UseExternalSuggestionsUseCases(
    private val settingsRepository: SettingsRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    open fun get() = settingsRepository.settings.mapLatest { it.useExternalSuggestions }
    open suspend fun set(value: Boolean) = settingsRepository.change { it.copy(useExternalSuggestions = value) }
    open suspend fun toggle() = settingsRepository.change {
        it.copy(useExternalSuggestions = it.useExternalSuggestions.not())
    }
}
