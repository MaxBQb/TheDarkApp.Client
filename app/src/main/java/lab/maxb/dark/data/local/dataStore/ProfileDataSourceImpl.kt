package lab.maxb.dark.data.local.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.google.crypto.tink.Aead
import io.github.osipxd.datastore.encrypted.encrypted
import lab.maxb.dark.data.local.dataStore.serializers.SerializersFactory
import lab.maxb.dark.domain.model.Profile
import org.koin.core.annotation.Singleton

@Singleton
class ProfileDataSourceImpl(
    private val context: Context,
    serializers: SerializersFactory,
    aead: Aead,
) : ProfileDataSource, DataStore<Profile?> by (
    DataStoreFactory.create(serializers.getFor<Profile?>().encrypted(aead)) {
        context.dataStoreFile("darkStore")
    }
) {
    override suspend fun clear() {
        updateData { null }
    }
}