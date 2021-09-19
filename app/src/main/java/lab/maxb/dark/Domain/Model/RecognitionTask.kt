package lab.maxb.dark.Domain.Model

import android.graphics.Bitmap

open class RecognitionTask(
    open var names: Set<String>? = null,
    open val image: Bitmap? = null,
    open val owner: User? = null
)