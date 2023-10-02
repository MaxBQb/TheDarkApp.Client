package lab.maxb.dark.data.repository

import lab.maxb.dark.data.local.datasource.SettingsLocalDataSource
import lab.maxb.dark.domain.model.Settings
import lab.maxb.dark.domain.repository.SettingsRepository
import org.koin.core.annotation.Singleton

@Singleton
class SettingsRepositoryImpl(
    private val localDataSource: SettingsLocalDataSource,
) : SettingsRepository {
    override val settings by localDataSource::data
    override suspend fun update(transform: suspend (Settings) -> Settings)
        = localDataSource.update(transform)
}