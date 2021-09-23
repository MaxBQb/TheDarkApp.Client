package lab.maxb.dark.Presentation.ViewModel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.Domain.Model.User
import lab.maxb.dark.Presentation.Repository.Repository
import android.graphics.BitmapFactory
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import kotlin.math.floor


class AddRecognitionTaskViewModel(application: Application) : AndroidViewModel(application) {
    private val thumbnailSize: Int = 1200
    var imageUri: Uri? = null

    fun addRecognitionTask(vararg names: String): Boolean {
        val namesSet = names.map { it.trim().lowercase() }
                     .filter { it.isNotBlank() }
                     .toSet()

        if (namesSet.isEmpty())
            return false

        val image = getThumbnail(imageUri) ?: return false

        viewModelScope.launch {
            Repository.getRepository().addRecognitionTask(RecognitionTask(
               namesSet, image, User("CURRENT_USER", 0)
            ))
        }
        return true
    }

    @Throws(FileNotFoundException::class, IOException::class)
    fun getThumbnail(uri: Uri?): Bitmap? {
        if (uri == null)
            return null
        val app = super.getApplication<Application>()
        var input: InputStream = app.contentResolver.openInputStream(uri)!!
        val onlyBoundsOptions = BitmapFactory.Options()
        onlyBoundsOptions.inJustDecodeBounds = true
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888 //optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions)
        input.close()
        if (onlyBoundsOptions.outWidth == -1 || onlyBoundsOptions.outHeight == -1) {
            return null
        }
        val originalSize =
            if (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) onlyBoundsOptions.outHeight else onlyBoundsOptions.outWidth
        val ratio: Double = if (originalSize > thumbnailSize) originalSize.toDouble().div(thumbnailSize.toDouble()) else 1.0
        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio)
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888 //
        input = app.contentResolver.openInputStream(uri)!!
        val bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions)
        input.close()
        return bitmap
    }

    private fun getPowerOfTwoForSampleRatio(ratio: Double): Int {
        val k = Integer.highestOneBit(floor(ratio).toInt())
        return if (k == 0) 1 else k
    }
}