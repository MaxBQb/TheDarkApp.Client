package lab.maxb.dark.data.repository

import android.content.Context
import androidx.datastore.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import lab.maxb.dark.data.local.dataStore.serializers.SettingsSerializer
import lab.maxb.dark.domain.model.Settings
import lab.maxb.dark.domain.repository.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    settingsSerializer: SettingsSerializer,
) : SettingsRepository {
    private val Context.localDataSource by dataStore("dark_settings", settingsSerializer)
    override val settings by context.localDataSource::data

    override suspend fun change(transform: suspend (Settings) -> Settings)
        = context.localDataSource.updateData(transform)

}