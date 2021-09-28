package lab.maxb.dark.Domain.Model

import android.graphics.Bitmap
import lab.maxb.dark.Domain.Operations.getUUID

open class RecognitionTask(
    open var names: Set<String>? = null,
    open var image: Bitmap? = null,
    open var owner: User? = null,
    open var id: String = getUUID(),
)