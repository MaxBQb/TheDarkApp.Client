package lab.maxb.dark.presentation.extra.delegates

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import lab.maxb.dark.R
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <reified T : ViewBinding> Fragment.viewBinding(): ReadOnlyProperty<Fragment, T> {
    return viewBinding {
        T::class.java.getMethod("bind", View::class.java).invoke(null, it) as T
    }
}

fun <T : ViewBinding> Fragment.viewBinding(bind: (View) -> T): ReadOnlyProperty<Fragment, T> {
    return object : ReadOnlyProperty<Fragment, T> {
        @Suppress("UNCHECKED_CAST")
        override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
            (requireView().getTag(R.id.view_binding_tag) as? T)?.let { return it }
            return bind(requireView()).also {
                requireView().setTag(R.id.view_binding_tag, it)
            }
        }
    }
}


inline fun <reified T : ViewBinding> FragmentActivity.viewBinding(): Lazy<T> {
    return viewBinding {
        T::class.java.getMethod("bind", View::class.java).invoke(null, it) as T
    }
}

fun <T : ViewBinding> FragmentActivity.viewBinding(bind: (View) -> T): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        val getContentView: FragmentActivity.() -> View = {
            checkNotNull(findViewById<ViewGroup>(android.R.id.content).getChildAt(0)) {
                "Call setContentView or Use Activity's secondary constructor passing layout res id."
            }
        }
        bind(getContentView())
    }
}

// Get from: https://github.com/wada811/ViewBinding-ktx
// Duplication reason: last release was incompatible with Java 1.8 for some reason
