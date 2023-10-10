package lab.maxb.dark.data.local.datasource

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.google.crypto.tink.Aead
import io.github.osipxd.datastore.encrypted.encrypted
import lab.maxb.dark.data.local.serializer.ProfileSerializer
import lab.maxb.dark.domain.model.Profile
import org.koin.core.annotation.Singleton

@Singleton
class ProfileDataStoreDataSource(
    private val context: Context,
    serializer: ProfileSerializer,
    aead: Aead,
) : ProfileLocalDataSource, BaseDataStoreDataSource<Profile?>(
    DataStoreFactory.create(serializer.encrypted(aead)) {
        context.dataStoreFile("darkStore")
    })