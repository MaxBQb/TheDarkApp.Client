package lab.maxb.dark.data.local.dataStore.serializers

import lab.maxb.dark.domain.model.Profile

class ProfileSerializer : BaseSerializer<Profile?>() {
    override val defaultValue: Profile? = null
    override val serializer = Profile.serializer()
}