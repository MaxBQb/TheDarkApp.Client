package lab.maxb.dark.Domain.Extra.Delegates

import kotlin.reflect.KProperty

class Once<T>() {
    private lateinit var mDefault: () -> T
    private var mValue: T? = null

    constructor(initializer: () -> T) : this() {
        mDefault = initializer
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>)
            = mValue ?: mDefault().also { mValue = it }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        mValue = value ?: value
    }
}