package lab.maxb.dark.data.local.dataStore.serializers

import lab.maxb.dark.domain.model.Profile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileSerializer @Inject constructor(): BaseSerializer<Profile?>() {
    override val defaultValue: Profile? = null
    override val serializer = Profile.serializer()
}