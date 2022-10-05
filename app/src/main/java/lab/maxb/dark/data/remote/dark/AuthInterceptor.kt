package lab.maxb.dark.data.remote.dark

import lab.maxb.dark.domain.repository.UserSettings
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.annotation.Single

@Single
class AuthInterceptor(
    private val userSettings: UserSettings
): Interceptor {
    val header = "Authorization"
    val value get() = "Bearer ${userSettings.token}"

    override fun intercept(chain: Interceptor.Chain): Response
        = with(chain.request().newBuilder()) {
            addHeader(header, value)
            chain.proceed(build())
        }
}
