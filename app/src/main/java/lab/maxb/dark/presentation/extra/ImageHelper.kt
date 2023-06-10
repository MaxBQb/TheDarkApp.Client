package lab.maxb.dark.presentation.extra

import android.content.Context
import android.content.Intent
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.core.annotation.Single


fun Uri.takePersistablePermission(context: Context) {
    context.applicationContext.contentResolver.takePersistableUriPermission(this,
        Intent.FLAG_GRANT_READ_URI_PERMISSION)
}

@Single
class ImageLoader(context: Context) {
    private var context = context.applicationContext

    suspend fun fromUri(uri: Uri): MultipartBody.Part = withContext(Dispatchers.IO) {
        val contentResolver = context.applicationContext.contentResolver
        val content = contentResolver.openInputStream(uri)!!.readBytes()
        return@withContext MultipartBody.Part.createFormData(
            "file",
            "filename.png",
            content.toRequestBody(
                contentResolver.getType(uri)!!.toMediaTypeOrNull(),
                0, content.size
            )
        )
    }
}