package lab.maxb.dark.domain.repository

import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.domain.model.Settings

interface SettingsRepository {
    val settings: Flow<Settings>
    suspend fun update(transform: suspend (Settings) -> Settings)
}
