package lab.maxb.dark.domain.repository

import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.data.datasource.local.SettingsLocalDataSource
import lab.maxb.dark.domain.model.Settings

interface SettingsRepository: SettingsLocalDataSource {
    val settings: Flow<Settings>
}
