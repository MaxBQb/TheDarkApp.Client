package lab.maxb.dark.Presentation.Room

import androidx.room.TypeConverter
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import java.lang.Exception


class Converters {
    @TypeConverter
    fun bitMapToString(bitmap: Bitmap?): ByteArray? {
        if (bitmap == null)
            return null
        ByteArrayOutputStream().let{
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            return it.toByteArray()
        }
    }

    @TypeConverter
    fun byteArrayToBitMap(bytes: ByteArray?): Bitmap? {
        return try {
            BitmapFactory.decodeByteArray(bytes, 0, bytes!!.size)
        } catch (e: Exception) {
            e.message
            null
        }
    }
}