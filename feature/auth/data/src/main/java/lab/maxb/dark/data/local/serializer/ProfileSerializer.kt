package lab.maxb.dark.data.local.serializer

import kotlinx.serialization.serializer
import lab.maxb.dark.data.local.model.ProfileLocalDTO
import org.koin.core.annotation.Factory


@Factory([ProfileSerializer::class])
class ProfileSerializer : BaseSerializer<ProfileLocalDTO?>() {
    override val defaultValue: ProfileLocalDTO? = null
    override val serializer = serializer<ProfileLocalDTO>()
}
