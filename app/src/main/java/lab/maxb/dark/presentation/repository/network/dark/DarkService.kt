package lab.maxb.dark.presentation.repository.network.dark

import lab.maxb.dark.BuildConfig
import lab.maxb.dark.presentation.repository.network.dark.routes.Auth
import lab.maxb.dark.presentation.repository.network.dark.routes.RecognitionTask
import lab.maxb.dark.presentation.repository.network.dark.routes.User
import okhttp3.OkHttpClient
import org.koin.core.annotation.Single
import org.koin.java.KoinJavaComponent.get
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface DarkService :
    RecognitionTask,
    User,
    Auth

@Single
class DarkServiceImpl: DarkService by buildDarkService()

private fun buildDarkService()
    = Retrofit.Builder()
        .baseUrl(BuildConfig.DARK_API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okhttpClient())
        .build()
        .create(DarkService::class.java)

private fun okhttpClient()
    = OkHttpClient.Builder()
        .addInterceptor(get(AuthInterceptor::class.java))
        .build()
