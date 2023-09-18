package lab.maxb.dark.data.repository

import lab.maxb.dark.data.datasource.local.SettingsLocalDataSource
import lab.maxb.dark.domain.repository.SettingsRepository
import org.koin.core.annotation.Singleton

@Singleton([SettingsRepository::class])
class SettingsRepositoryImpl(
    private val localDataSource: SettingsLocalDataSource,
) : SettingsRepository, SettingsLocalDataSource by localDataSource {
    override val settings by localDataSource::data
}