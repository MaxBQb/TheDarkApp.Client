package lab.maxb.dark.data.datasource.remote

import lab.maxb.dark.data.remote.dark.AuthInterceptor
import lab.maxb.dark.data.remote.dark.routes.AuthAPI
import org.koin.core.annotation.Singleton

@Singleton
class AuthRetrofitDataSource(
    private val api: AuthAPI,
    authInterceptor: AuthInterceptor,
): AuthRemoteDataSource by api {
    override val authState = authInterceptor.authState
}