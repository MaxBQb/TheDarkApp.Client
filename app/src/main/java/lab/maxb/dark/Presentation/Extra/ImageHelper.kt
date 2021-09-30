package lab.maxb.dark.Presentation.Extra

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri

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