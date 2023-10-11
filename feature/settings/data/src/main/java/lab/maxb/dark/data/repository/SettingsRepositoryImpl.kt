package lab.maxb.dark.data.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import lab.maxb.dark.data.local.datasource.SettingsLocalDataSource
import lab.maxb.dark.data.local.model.toDomain
import lab.maxb.dark.data.local.model.toLocalDTO
import lab.maxb.dark.domain.model.Settings
import lab.maxb.dark.domain.repository.SettingsRepository
import org.koin.core.annotation.Singleton

@Singleton
class SettingsRepositoryImpl(
    private val localDataSource: SettingsLocalDataSource,
) : SettingsRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override val settings = localDataSource.data.mapLatest { it.toDomain() }
    override suspend fun update(transform: suspend (Settings) -> Settings)
        = localDataSource.update { transform(it.toDomain()).toLocalDTO() }
}