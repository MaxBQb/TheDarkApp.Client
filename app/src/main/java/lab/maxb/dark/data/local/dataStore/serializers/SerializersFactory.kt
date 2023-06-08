package lab.maxb.dark.data.local.dataStore.serializers

import androidx.datastore.core.Serializer
import org.koin.core.annotation.Single

@Single
class SerializersFactory {
    val serializers = listOf(
        SettingsSerializer(),
        ProfileSerializer(),
    )
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> getFor(): Serializer<T> = serializers.first {
        it.defaultValue is T
    } as Serializer<T>
}