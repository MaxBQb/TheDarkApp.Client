package lab.maxb.dark.data.remote

import okhttp3.logging.HttpLoggingInterceptor




internal val logger get() = HttpLoggingInterceptor().apply {
    setLevel(HttpLoggingInterceptor.Level.BASIC)
}