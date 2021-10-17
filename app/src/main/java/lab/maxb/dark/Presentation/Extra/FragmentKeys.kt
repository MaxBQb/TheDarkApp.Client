package lab.maxb.dark.Presentation.Extra

import kotlin.reflect.KClass

class FragmentKeys(clazz: KClass<*>) {
    val path = clazz.qualifiedName!!
    private val params = mutableMapOf<String, String>()

    fun param(name: String)
        = "$path.params.$name".also {
            params[it] = name
        }

    fun request(paramName: String) = "$path.request.${params[paramName]}"

    fun response(paramName: String) = "$path.response.${params[paramName]}"

    fun clear() = params.clear()
}