package lab.maxb.dark.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.annotation.Factory

@Factory([LoggingInterceptor::class])
@JvmInline
value class LoggingInterceptorImpl(
    private val interceptor: Interceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BASIC)
    }
) : LoggingInterceptor, Interceptor by interceptor