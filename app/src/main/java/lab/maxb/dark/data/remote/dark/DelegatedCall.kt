package lab.maxb.dark.data.remote.dark

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DelegatedCall<T>(
    private val delegate: Call<T>,
    private val onResult: DelegatedCall<T>.DelegatedCallback.(result: Result<Response<T>>) -> Unit,
) : Call<T> {
    inner class DelegatedCallback(private val callback: Callback<T>) {
        fun onResponse(response: Response<T>)
            = callback.onResponse(this@DelegatedCall, response)
        fun onFailure(t: Throwable) = callback.onFailure(this@DelegatedCall, t)
    }
    override fun enqueue(callback: Callback<T>) {
        val wrappedCallback = DelegatedCallback(callback)
        delegate.enqueue(
            object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    onResult(wrappedCallback, Result.success(response))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    onResult(wrappedCallback, Result.failure(t))
                }
            }
        )
    }

    override fun isExecuted() = delegate.isExecuted
    override fun execute()= Response.success(delegate.execute().body())
    override fun cancel() = delegate.cancel()
    override fun clone() = DelegatedCall(delegate.clone(), onResult)
    override fun isCanceled() = delegate.isCanceled
    override fun request() = delegate.request()
    override fun timeout() = delegate.timeout()
}
