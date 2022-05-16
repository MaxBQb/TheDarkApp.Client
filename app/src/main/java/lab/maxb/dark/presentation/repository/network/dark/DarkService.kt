package lab.maxb.dark.presentation.repository.network.dark

import com.google.gson.GsonBuilder
import lab.maxb.dark.BuildConfig
import lab.maxb.dark.presentation.repository.network.dark.routes.Auth
import lab.maxb.dark.presentation.repository.network.dark.routes.RecognitionTask
import lab.maxb.dark.presentation.repository.network.dark.routes.User
import lab.maxb.dark.presentation.repository.network.logger
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.koin.core.annotation.Single
import org.koin.java.KoinJavaComponent.get
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type


interface DarkService :
    RecognitionTask,
    User,
    Auth

@Single
class DarkServiceImpl: DarkService by buildDarkService()

private fun buildDarkService()
    = Retrofit.Builder()
        .baseUrl(BuildConfig.DARK_API_URL)
        .addConverterFactory(converter)
        .client(okhttpClient)
        .build()
        .create(DarkService::class.java)

private val okhttpClient get() = OkHttpClient.Builder()
    .addInterceptor(logger)
    .addInterceptor(get<AuthInterceptor>(AuthInterceptor::class.java))
    .build()

private val converter get() = GsonBuilder().apply {
    setLenient()
}.create().run {
    GsonConverterFactory.create(this)
}