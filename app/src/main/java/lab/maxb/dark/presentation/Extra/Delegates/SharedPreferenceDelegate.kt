package lab.maxb.dark.presentation.Extra.Delegates

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Creates delegate for property from SharedPreferences with the key presented as className+propertyName.
 * @param T type of the property
 * @param defaultValue is used if property not set
 * @return wrapper for property accessors
 */
inline fun <reified T> SharedPreferences.property(defaultValue: T = defaultForType()) =
    object : ReadWriteProperty<Any, T> {
        private var mValue: T? = null

        override fun getValue(thisRef: Any, property: KProperty<*>) = mValue ?: this@property[
                getKey(thisRef, property),
                defaultValue
        ].also { mValue = it }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T)
                = put(getKey(thisRef, property), value.also { mValue = it })

        private fun getKey(thisRef: Any, property: KProperty<*>)
                = "${thisRef.javaClass.name}.${property.name}"
    }

/**
 * Set property [value] to shared preferences by [key].
 * @param T type of the property
 */
inline fun <reified T> SharedPreferences.put(key: String, value: T)
        = edit { put(key, value) }

/**
 * Set property [value] to shared preferences by [key].
 * @param T type of the property
 */
inline fun <reified T> SharedPreferences.Editor.put(key: String, value: T): SharedPreferences.Editor
        = when (value) {
    is String -> putString(key, value)
    is Int -> putInt(key, value)
    is Boolean -> putBoolean(key, value)
    is Float -> putFloat(key, value)
    is Long -> putLong(key, value)
    else -> throw UnsupportedOperationException("Type ${T::class} is not supported yet")
}

/**
 * Get property value by [key]. If property not set return [defaultValue] ("", true, 0 if not given).
 * @param T type of the property
 * @return saved value or [defaultValue].
 */
inline operator fun <reified T> SharedPreferences.get(key: String, defaultValue: T = defaultForType()) =
    when (defaultValue) {
        is String -> getString(key, defaultValue) as T
        is Int -> getInt(key, defaultValue) as T
        is Boolean -> getBoolean(key, defaultValue) as T
        is Float -> getFloat(key, defaultValue) as T
        is Long -> getLong(key, defaultValue) as T
        else -> throw UnsupportedOperationException("Type ${T::class} is not supported yet")
    }

/**
 * Default value for typical property types.
 * @param T [String] -> empty string
 * @param T [Int] / [Float] / [Long] -> 0
 * @param T [Boolean] -> false
 */
inline fun <reified T> defaultForType(): T =
    when (T::class) {
        String::class -> "" as T
        Int::class -> 0 as T
        Boolean::class -> false as T
        Float::class -> 0F as T
        Long::class -> 0L as T
        else -> throw IllegalArgumentException("Default value not found for type ${T::class}")
    }
