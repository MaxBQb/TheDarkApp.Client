package lab.maxb.dark.Presentation.Extra

import lab.maxb.dark.Domain.Operations.getUUID
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class FragmentKeys(clazz: KClass<*>) {
    private val path = clazz.qualifiedName!!

    fun param(): Path = Path(::param.name)
    fun communication(): Path = Path(::communication.name)
    fun special(): Path = Path(::special.name)

    inner class Path(private val key: String) {
        private var name: String? = null

        operator fun getValue(thisRef: Any?, property: KProperty<*>)
            = name ?: "$path.$key.${property.name}.${getUUID()}".also { name = it }
    }
}