package lab.maxb.dark.Presentation.Repository.Room

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream


class Converters {
    @TypeConverter
    fun bitMapToByteArray(bitmap: Bitmap?): ByteArray? {
        if (bitmap == null)
            return null
        ByteArrayOutputStream().let{
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            return it.toByteArray()
        }
    }

    @TypeConverter
    fun byteArrayToBitMap(bytes: ByteArray?): Bitmap? = try {
        BitmapFactory.decodeByteArray(bytes, 0, bytes!!.size)
    } catch (e: Exception) {
        e.message
        null
    }
}