package lab.maxb.dark.data.remote.dark

import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.google.gson.GsonBuilder
import lab.maxb.dark.BuildConfig
import lab.maxb.dark.data.model.remote.AuthRequest
import lab.maxb.dark.data.model.remote.RecognitionTaskCreationNetworkDTO
import lab.maxb.dark.data.remote.logger
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import org.koin.core.annotation.Single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.EOFException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@Single
class DarkServiceImpl(
    private val authInterceptor: AuthInterceptor
) : DarkService {
    private val api = buildDarkService()
    override var onAuthRequired: (suspend () -> Unit)? = null

    override suspend fun getAllTasks(page: Int, size: Int) = catchAll {
        api.getAllTasks(page, size)
    }

    override suspend fun getTask(id: String) = catchAll {
        api.getTask(id)
    }

    override suspend fun addTask(task: RecognitionTaskCreationNetworkDTO) = catchAll {
        api.addTask(task)
    }

    override suspend fun markTask(id: String, isAllowed: Boolean) = catchAll {
        api.markTask(id, isAllowed)
    }

    override suspend fun solveTask(id: String, answer: String) = catchAll {
        api.solveTask(id, answer)
    }

    override suspend fun addImage(id: String, filePart: MultipartBody.Part) = catchAll {
        api.addImage(id, filePart)
    }

    override fun getImageSource(path: String) = GlideUrl(
        "${BuildConfig.DARK_API_URL}/task/image/$path",
        LazyHeaders.Builder()
            .addHeader(authInterceptor.header, authInterceptor.value)
            .build()
    )

    override suspend fun getUser(id: String) = catchAll {
        api.getUser(id)
    }

    override suspend fun login(request: AuthRequest) = catchAll {
        api.login(request)
    }

    override suspend fun signup(request: AuthRequest) = catchAll {
        api.signup(request)
    }

    private suspend inline fun<reified T> catchAll(
        crossinline block: suspend () -> T,
    ): T = try {
        retry(block)
    } catch (e: EOFException) {
        null as T
    } catch (e: UnableToObtainResource) {
        throw e
    } catch (e: UnknownHostException) {
        throw UnableToObtainResource()
    } catch (e: ConnectException) {
        throw UnableToObtainResource()
    } catch (e: retrofit2.HttpException) {
        when (e.code()) {
            401, 403 -> onAuthRequired?.invoke()
            else -> e.printStackTrace()
        }
        throw UnableToObtainResource()
    } catch (e: Throwable) {
        e.printStackTrace()
        throw e
    }

    private suspend inline fun<T> retry(
        crossinline block: suspend () -> T,
    ): T {
        repeat(MAX_RETRY_COUNT) {
            try {
                return block()
            } catch (e: SocketTimeoutException) {
                // Ignore (delay included in timeout)
            }
        }
        throw UnableToObtainResource()
    }

    // Initialization
    private fun buildDarkService()
            = Retrofit.Builder()
        .baseUrl(BuildConfig.DARK_API_URL)
        .addConverterFactory(converter)
        .client(okhttpClient)
        .build()
        .create(DarkServiceAPI::class.java)

    private val okhttpClient get() = OkHttpClient.Builder()
        .addInterceptor(logger)
        .addInterceptor(authInterceptor)
        .build()

    private val converter get() = GsonBuilder().apply {
        setLenient()
    }.create().run {
        GsonConverterFactory.create(this)
    }

    companion object {
        const val MAX_RETRY_COUNT = 6
    }
}


class UnableToObtainResource: Exception()