package lab.maxb.dark.data.local.serializer

import lab.maxb.dark.domain.model.Settings
import org.koin.core.annotation.Factory

@Factory([SettingsSerializer::class])
class SettingsSerializer : BaseSerializer<Settings>() {
    override val defaultValue = Settings()
    override val serializer = Settings.serializer()
}