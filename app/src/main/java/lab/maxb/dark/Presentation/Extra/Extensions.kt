package lab.maxb.dark.Presentation.Extra

import android.os.Bundle
import android.view.View
import android.view.View.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

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

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}