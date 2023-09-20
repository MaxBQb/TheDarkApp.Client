package lab.maxb.dark.data.remote.dark

import android.util.Log
import com.google.gson.GsonBuilder
import lab.maxb.dark.BuildConfig
import lab.maxb.dark.data.remote.dark.RetrofitDarkClient.buildDarkService
import lab.maxb.dark.data.remote.dark.routes.ArticlesAPI
import lab.maxb.dark.data.remote.dark.routes.AuthAPI
import lab.maxb.dark.data.remote.dark.routes.ImagesAPI
import lab.maxb.dark.data.remote.dark.routes.RecognitionTasksAPI
import lab.maxb.dark.data.remote.dark.routes.UsersAPI
import lab.maxb.dark.data.remote.logger
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

    val errorHandler = buildDelegatedCallAdapterFactory {
        it.onSuccess { response ->
            if (response.isSuccessful)
                onResponse(Response.success(response.code(), response.body()))
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
            .addInterceptor(logger)
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
    RecognitionTasksAPI::class,
    ImagesAPI::class,
    UsersAPI::class,
    AuthAPI::class,
])
class DarkServiceImpl : DarkServiceAPI by buildDarkService()
