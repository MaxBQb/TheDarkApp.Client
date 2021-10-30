package lab.maxb.dark.Presentation.Extra.Delegates

import android.content.Context
import androidx.core.content.edit
import kotlin.reflect.KProperty

class SharedPreferenceDelegate(
    context: Context,
    private val sectionKey: String,
) {
    private val context = context.applicationContext

    private var _value: String? = null

    private fun getPrefs(context: Context)
        = context.getSharedPreferences(sectionKey, Context.MODE_PRIVATE)

    operator fun getValue(thisRef: Any?, property: KProperty<*>)
        = _value ?: getPrefs(context)
            ?.getString(property.name, null)
            ?.also { _value = it }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) {
        if (_value == value)
            return
        _value = value
        getPrefs(context).edit {
            putString(property.name, _value)
        }
    }
}

infix fun Context.shared(sectionKey: String)
    = SharedPreferenceDelegate(this, sectionKey)