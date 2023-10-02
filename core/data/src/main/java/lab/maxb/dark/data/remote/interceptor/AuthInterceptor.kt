package lab.maxb.dark.data.remote.interceptor

import kotlinx.coroutines.flow.SharedFlow
import lab.maxb.dark.domain.model.AuthState
import okhttp3.Interceptor

interface AuthInterceptor : Interceptor {
    val authState : SharedFlow<AuthState>
}
