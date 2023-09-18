package lab.maxb.dark.data.datasource.local

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import lab.maxb.dark.data.local.dataStore.serializers.SettingsSerializer
import lab.maxb.dark.domain.model.Settings
import org.koin.core.annotation.Singleton

@Singleton
class SettingsDataStoreDataSource(
    private val context: Context,
    serializer: SettingsSerializer,
) : SettingsLocalDataSource, BaseDataStoreDataSource<Settings>(
    DataStoreFactory.create(serializer) {
        context.dataStoreFile("dark_settings")
    }
)