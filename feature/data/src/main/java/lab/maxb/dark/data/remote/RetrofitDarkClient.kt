package lab.maxb.dark.data.remote

import android.util.Log
import com.google.gson.GsonBuilder
import lab.maxb.dark.data.BuildConfig
import lab.maxb.dark.data.exception.UnableToObtainResource
import lab.maxb.dark.data.remote.RetrofitDarkClient.buildDarkService
import lab.maxb.dark.data.remote.interceptor.AuthInterceptor
import lab.maxb.dark.data.remote.interceptor.LoggingInterceptor
import lab.maxb.dark.data.remote.route.ArticlesAPI
import lab.maxb.dark.data.remote.route.AuthAPI
import lab.maxb.dark.data.remote.route.DarkServiceAPI
import lab.maxb.dark.data.remote.route.UsersAPI
import lab.maxb.dark.data.remote.utils.buildDelegatedCallAdapterFactory
import okhttp3.OkHttpClient
import org.koin.core.annotation.Singleton
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.EOFException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit


object RetrofitDarkClient : KoinComponent {
    const val TIMEOUT = 90L
    private val authInterceptor: AuthInterceptor by inject()
    private val loggingInterceptor: LoggingInterceptor by inject()

    val errorHandler = buildDelegatedCallAdapterFactory {
        it.onSuccess { response ->
            if (response.isSuccessful) {
                onResponse(Response.success(response.code(), response.body()))
                return@onSuccess
            }
            when (response.code()) {
                401, 403 -> {}
                404 -> onResponse(Response.success(null))
                // TODO: Handle 400 and 422
                else -> Log.e(
                    "Retrofit",
                    "Error ${response.code()}, resolved as 'Unable to obtain resource'."
                )
            }
            onFailure(UnableToObtainResource())
        }.onFailure { e ->
            when (e) {
                is EOFException -> onResponse(Response.success(null))
                is UnknownHostException,
                is SocketTimeoutException,
                is ConnectException,
                -> onFailure(UnableToObtainResource())

                else -> onFailure(e)
            }
        }
    }

    internal fun buildDarkService() = Retrofit.Builder()
        .baseUrl(BuildConfig.DARK_API_URL)
        .addConverterFactory(converter)
        .addCallAdapterFactory(errorHandler)
        .client(okhttpClient)
        .build()
        .create(DarkServiceAPI::class.java)

    private val okhttpClient
        get() = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()

    private val converter
        get() = GsonBuilder()
            .apply { setLenient() }
            .create()
            .let { GsonConverterFactory.create(it) }

}

@Singleton([
    DarkServiceAPI::class,
    ArticlesAPI::class,
    lab.maxb.dark.data.remote.route.RecognitionTasksAPI::class,
    lab.maxb.dark.data.remote.route.ImagesAPI::class,
    UsersAPI::class,
    AuthAPI::class,
])
internal fun buildDarkService() : DarkServiceAPI = buildDarkService()
