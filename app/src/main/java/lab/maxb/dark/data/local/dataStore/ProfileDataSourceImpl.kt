package lab.maxb.dark.data.local.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.google.crypto.tink.Aead
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.osipxd.datastore.encrypted.encrypted
import lab.maxb.dark.data.local.dataStore.serializers.ProfileSerializer
import lab.maxb.dark.domain.model.Profile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    profileSerializer: ProfileSerializer,
    aead: Aead,
) : ProfileDataSource, DataStore<Profile?> by (
    DataStoreFactory.create(profileSerializer.encrypted(aead)) {
        context.dataStoreFile("darkStore")
    }
) {
    override suspend fun clear() {
        updateData { null }
    }
}