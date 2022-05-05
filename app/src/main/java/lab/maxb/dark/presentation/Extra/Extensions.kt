package lab.maxb.dark.presentation.Extra

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.View.*
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener

fun View.hide(preserveSpace: Boolean = false) {
    visibility = if (preserveSpace) INVISIBLE else GONE
}

fun View.show() { visibility = VISIBLE }

fun View.toggleVisibility(isVisible: Boolean? = null, preserveSpace: Boolean = false) {
    val newVisibility = isVisible ?: (visibility != VISIBLE)
    if (newVisibility)
        show()
    else
        hide(preserveSpace)
}

fun Fragment.requestFragmentResult(key: String)
    = setFragmentResult(key, Bundle())

fun Fragment.setFragmentResponse(requestKey: String,
                                 responseKey: String,
                                 response: () -> Bundle) {
    setFragmentResultListener(requestKey) { _, _ ->
        setFragmentResult(responseKey, response())
    }
}

fun Fragment.withArgs(vararg args: Pair<String, Any?>)
    = apply { arguments = bundleOf(*args) }

fun EditText.setPasswordVisibility(visible: Boolean) {
    inputType = InputType.TYPE_CLASS_TEXT or if (visible)
        InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
    else
        InputType.TYPE_TEXT_VARIATION_PASSWORD
}