package lab.maxb.dark.Domain.Model

import android.graphics.Bitmap
import java.util.*

open class RecognitionTask(
    open var names: Set<String>? = null,
    open val image: Bitmap? = null,
    open val owner: User? = null,
    open val id: String = UUID.randomUUID().toString(),
)