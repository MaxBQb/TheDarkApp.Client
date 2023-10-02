package lab.maxb.dark.data.remote.utils

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class DelegatedCallAdapterFactory(
    val delegatedCallFactory: (call: Call<Any>) -> Call<*>
) : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java || returnType !is ParameterizedType) {
            return null
        }

        return object : CallAdapter<Any, Call<*>> {
            override fun responseType(): Type = getParameterUpperBound(0, returnType)
            override fun adapt(call: Call<Any>): Call<*> = delegatedCallFactory(call)
        }
    }
}

fun buildDelegatedCallAdapterFactory(
    block: DelegatedCall<Any>.DelegatedCallback.(result: Result<Response<Any>>) -> Unit
) = DelegatedCallAdapterFactory {
    DelegatedCall(it, block)
}