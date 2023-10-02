package lab.maxb.dark.data.local.serializer

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

abstract class BaseSerializer<T>: Serializer<T> {
    abstract val serializer: KSerializer<T & Any>

    override suspend fun readFrom(input: InputStream) = try {
        Json.decodeFromString(
            deserializer = serializer,
            string = input.readBytes().decodeToString()
        )
    } catch (e: SerializationException) {
        e.printStackTrace()
        defaultValue
    }

    override suspend fun writeTo(t: T, output: OutputStream) = withContext(Dispatchers.IO) {
        output.write(
            Json.encodeToString(
                serializer = serializer,
                value = t ?: return@withContext,
            ).encodeToByteArray()
        )
    }
}