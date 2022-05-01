package lab.maxb.dark.Presentation.Repository.Network.Dark

import lab.maxb.dark.Presentation.Repository.Network.Dark.Groups.Auth
import lab.maxb.dark.Presentation.Repository.Network.Dark.Groups.RecognitionTask
import lab.maxb.dark.Presentation.Repository.Network.Dark.Groups.User
import okhttp3.OkHttpClient
import org.koin.core.annotation.Single
import org.koin.java.KoinJavaComponent.get
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface DarkService :
    RecognitionTask,
    User,
    Auth

const val DARK_SERVICE_URL = "https://polar-anchorage-77657.herokuapp.com"

@Single
class DarkServiceImpl: DarkService by buildDarkService()

private fun buildDarkService()
    = Retrofit.Builder()
        .baseUrl(DARK_SERVICE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okhttpClient())
        .build()
        .create(DarkService::class.java)

private fun okhttpClient()
    = OkHttpClient.Builder()
        .addInterceptor(get(AuthInterceptor::class.java))
        .build()
