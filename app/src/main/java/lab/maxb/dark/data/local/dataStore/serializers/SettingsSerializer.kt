package lab.maxb.dark.data.local.dataStore.serializers

import lab.maxb.dark.domain.model.Settings
import org.koin.core.annotation.Singleton

@Singleton
class SettingsSerializer : BaseSerializer<Settings>() {
    override val defaultValue = Settings()
    override val serializer = Settings.serializer()
}