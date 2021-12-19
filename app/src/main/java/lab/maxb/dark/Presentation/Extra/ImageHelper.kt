package lab.maxb.dark.Presentation.Extra

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.InputStream

private fun getContentResolver(context: Context) = context.applicationContext.contentResolver

private fun bitmapFromUri(uri: Uri,
                          context: Context,
                          opts: BitmapFactory.Options): Bitmap?
        = BitmapFactory.decodeFileDescriptor(
    getContentResolver(context).openFileDescriptor(
        uri, "r"
    )?.fileDescriptor,
    null,
    opts
)

fun Uri.takePersistablePermission(context: Context) {
    getContentResolver(context).takePersistableUriPermission(this,
        Intent.FLAG_GRANT_READ_URI_PERMISSION)
}

fun Uri.toBitmap(
    context: Context,
    reqWidth: Int,
    reqHeight: Int
): Bitmap?
    // First decode with inJustDecodeBounds=true to check dimensions
    = try {BitmapFactory.Options().run {
        inJustDecodeBounds = true
        bitmapFromUri(this@toBitmap, context, this)

        // Calculate inSampleSize
        inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        inJustDecodeBounds = false

        bitmapFromUri(this@toBitmap, context, this) ?: return null
    }} catch (exception: Throwable) { null }

private fun calculateInSampleSize(options: BitmapFactory.Options,
                                  reqWidth: Int,
                                  reqHeight: Int): Int {
    // Raw height and width of image
    val (height: Int, width: Int) = options.run {
        outHeight to outWidth
    }
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {

        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}

class ImageLoader(context: Context) {
    private var context = context.applicationContext

    fun fromUri(uri: Uri): MultipartBody.Part {
        val contentResolver = getContentResolver(context)
        return MultipartBody.Part.createFormData(
            "file",
            "filename.png",
            RequestBody.create(
                MediaType.parse(contentResolver.getType(uri)!!),
                contentResolver.openInputStream(uri)!!.readBytes()
            )
        )
    }

    fun fromResponse(body: ResponseBody?, filename: String): String {
        body ?: return ""

        val path = "image_$filename"

        var input: InputStream? = null
        try {
            input = body.byteStream()
            context.openFileOutput(path, Context.MODE_PRIVATE).use { output ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (input.read(buffer).also { read = it } != -1)
                    output.write(buffer, 0, read)
                output.flush()
            }
            return Uri.fromFile(
                context.getFileStreamPath(path)
            ).toString()
        } catch (e: Exception){
            e.printStackTrace()
        } finally {
            input?.close()
        }
        return ""
    }
}