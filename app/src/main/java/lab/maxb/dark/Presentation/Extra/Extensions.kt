package lab.maxb.dark.Presentation.Extra

import android.view.View
import android.view.View.*

fun View.hide(preserveSpace: Boolean = false) {
    visibility = if (preserveSpace) INVISIBLE else GONE
}

fun View.show() {
    visibility = VISIBLE
}

fun View.toggleVisibility(isVisible: Boolean? = null, preserveSpace: Boolean = false) {
    val newVisibility = isVisible ?: (visibility != VISIBLE)
    if (newVisibility)
        show()
    else
        hide(preserveSpace)
}
