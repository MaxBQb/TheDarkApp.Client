package lab.maxb.dark.data.local.serializer

import kotlinx.serialization.serializer
import lab.maxb.dark.data.local.model.SettingsLocalDTO
import lab.maxb.dark.data.local.model.toLocalDTO
import lab.maxb.dark.domain.model.Settings
import org.koin.core.annotation.Factory

@Factory([SettingsSerializer::class])
class SettingsSerializer : BaseSerializer<SettingsLocalDTO>() {
    override val defaultValue = Settings().toLocalDTO()
    override val serializer = serializer<SettingsLocalDTO>()
}