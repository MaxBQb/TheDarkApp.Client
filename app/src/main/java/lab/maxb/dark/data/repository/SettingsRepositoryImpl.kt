package lab.maxb.dark.data.repository

import android.content.Context
import androidx.datastore.dataStore
import lab.maxb.dark.data.local.dataStore.serializers.SerializersFactory
import lab.maxb.dark.domain.model.Settings
import lab.maxb.dark.domain.repository.SettingsRepository
import org.koin.core.annotation.Singleton

@Singleton
class SettingsRepositoryImpl(
    private val context: Context,
    serializers: SerializersFactory,
) : SettingsRepository {
    private val Context.localDataSource by dataStore("dark_settings", serializers.getFor<Settings>())
    override val settings by context.localDataSource::data

    override suspend fun change(transform: suspend (Settings) -> Settings)
        = context.localDataSource.updateData(transform)

}