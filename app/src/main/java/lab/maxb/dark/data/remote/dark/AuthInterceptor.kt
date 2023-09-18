package lab.maxb.dark.data.remote.dark

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import lab.maxb.dark.data.datasource.local.ProfileLocalDataSource
import lab.maxb.dark.domain.model.AuthState
import lab.maxb.dark.domain.model.withToken
import lab.maxb.dark.domain.model.withoutToken
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.annotation.Single

@Single
class AuthInterceptor(
    profileDataSource: ProfileLocalDataSource,
): Interceptor {
    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    private val token = profileDataSource.data.mapLatest { it?.token ?: "" }
        .onEach { token -> _authState.update { it.withToken(token) } }
        .stateIn(
            GlobalScope,
            SharingStarted.Eagerly,
            ""
        )

    val header = "Authorization"
    val value get() = "Bearer ${token.value}"
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState = _authState.asSharedFlow()

    override fun intercept(chain: Interceptor.Chain): Response
        = with(chain.request().newBuilder()) {
            addHeader(header, value)
            try {
                chain.proceed(build())
            } catch (e: retrofit2.HttpException) {
                when (e.code()) {
                    401, 403 -> _authState.update { it.withoutToken() }
                }
                throw e
            }
    }
}
