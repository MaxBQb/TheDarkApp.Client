package lab.maxb.dark.data.remote.dark

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import lab.maxb.dark.data.local.dataStore.ProfileDataSource
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.annotation.Single

@Single
class AuthInterceptor(
    profileDataSource: ProfileDataSource,
): Interceptor {
    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    private val token = profileDataSource.data.mapLatest { it?.token ?: "" }
        .stateIn(
            GlobalScope,
            SharingStarted.Eagerly,
            ""
        )

    val header = "Authorization"
    val value get() = "Bearer ${token.value}"

    override fun intercept(chain: Interceptor.Chain): Response
        = with(chain.request().newBuilder()) {
            addHeader(header, value)
            chain.proceed(build())
        }
}
