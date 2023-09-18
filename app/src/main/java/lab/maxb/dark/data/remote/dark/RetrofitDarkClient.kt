package lab.maxb.dark.data.remote.dark

import com.google.gson.GsonBuilder
import kotlinx.coroutines.CancellationException
import lab.maxb.dark.BuildConfig
import lab.maxb.dark.data.remote.logger
import okhttp3.OkHttpClient
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.EOFException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit


@Single
object RetrofitDarkClient : KoinComponent {
    const val TIMEOUT = 90L
    private val authInterceptor: AuthInterceptor by inject()

    private inline fun <reified T> catchAll(
        crossinline block: () -> T,
    ): T {
        try {
            return block()
        } catch (e: EOFException) {
            return null as T
        } catch (e: UnableToObtainResource) {
            throw e
        } catch (e: CancellationException) {
            throw e
        } catch (e: retrofit2.HttpException) {
            when (e.code()) {
                404 -> return null as T
                // TODO: Handle 400 and 422
                else -> e.printStackTrace()
            }
            throw UnableToObtainResource()
        } catch (e: Throwable) {
            when (e) {
                is UnknownHostException,
                is SocketTimeoutException,
                is ConnectException,
                -> throw UnableToObtainResource()
            }
            e.printStackTrace()
            throw e
        }
    }

    internal fun buildDarkService() = Retrofit.Builder()
        .baseUrl(BuildConfig.DARK_API_URL)
        .addConverterFactory(converter)
        .client(okhttpClient)
        .build()
        .create(DarkServiceAPI::class.java)

    private val okhttpClient
        get() = OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor(authInterceptor)
            .addInterceptor { catchAll { it.proceed(it.request()) } }
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


class UnableToObtainResource : Exception()