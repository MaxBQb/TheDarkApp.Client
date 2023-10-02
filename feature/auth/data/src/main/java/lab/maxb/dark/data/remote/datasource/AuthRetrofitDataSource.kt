package lab.maxb.dark.data.remote.datasource

import lab.maxb.dark.data.remote.interceptor.AuthInterceptor
import lab.maxb.dark.data.remote.route.AuthAPI
import org.koin.core.annotation.Singleton

@Singleton
class AuthRetrofitDataSource(
    private val api: AuthAPI,
    authInterceptor: AuthInterceptor,
): AuthRemoteDataSource by api {
    override val authState = authInterceptor.authState
}