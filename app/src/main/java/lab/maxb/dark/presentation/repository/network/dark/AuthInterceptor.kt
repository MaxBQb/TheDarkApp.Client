package lab.maxb.dark.presentation.repository.network.dark

import lab.maxb.dark.presentation.extra.UserSettings
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.annotation.Single

@Single
class AuthInterceptor(
    private val userSettings: UserSettings
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response
        = with(chain.request().newBuilder()) {
            addHeader("Authorization", "Bearer ${userSettings.token}")
            chain.proceed(build())
        }
}
