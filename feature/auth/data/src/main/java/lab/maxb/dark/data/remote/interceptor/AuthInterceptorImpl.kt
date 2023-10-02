package lab.maxb.dark.data.remote.interceptor

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import lab.maxb.dark.data.local.datasource.ProfileLocalDataSource
import lab.maxb.dark.domain.model.AuthState
import lab.maxb.dark.domain.model.withToken
import lab.maxb.dark.domain.model.withoutToken
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.annotation.Single

@Single
class AuthInterceptorImpl(
    profileDataSource: ProfileLocalDataSource,
): AuthInterceptor {
    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    private val token = profileDataSource.data.mapLatest { it?.token ?: "" }
        .onEach { token -> authState.update { it.withToken(token) } }
        .stateIn(
            GlobalScope,
            SharingStarted.Eagerly,
            ""
        )

    private val value get() = "Bearer ${token.value}"
    override val authState = MutableStateFlow<AuthState>(AuthState.Loading)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(HEADER, value)
            .build()
        val result = chain.proceed(request)
        when (result.code) {
            401, 403 -> authState.update { it.withoutToken() }
        }
        return result
    }

    companion object {
        private const val HEADER = "Authorization"
    }
}