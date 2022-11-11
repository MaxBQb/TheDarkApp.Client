package lab.maxb.dark.data.local.dataStore.serializers

import lab.maxb.dark.domain.model.Settings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsSerializer @Inject constructor(): BaseSerializer<Settings>() {
    override val defaultValue = Settings()
    override val serializer = Settings.serializer()
}