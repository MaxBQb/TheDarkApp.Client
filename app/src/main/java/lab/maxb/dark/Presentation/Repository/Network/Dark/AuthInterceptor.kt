package lab.maxb.dark.Presentation.Repository.Network.Dark

import lab.maxb.dark.Presentation.Extra.SessionHolder
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val sessionHolder: SessionHolder
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        sessionHolder.token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}
