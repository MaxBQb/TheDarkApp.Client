package lab.maxb.dark.data.local.serializer

import lab.maxb.dark.domain.model.Profile
import org.koin.core.annotation.Factory

@Factory([ProfileSerializer::class])
class ProfileSerializer : BaseSerializer<Profile?>() {
    override val defaultValue: Profile? = null
    override val serializer = Profile.serializer()
}