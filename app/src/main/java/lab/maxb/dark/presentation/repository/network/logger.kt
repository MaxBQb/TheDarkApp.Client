package lab.maxb.dark.presentation.repository.network

import okhttp3.logging.HttpLoggingInterceptor




internal val logger get() = HttpLoggingInterceptor().apply {
    setLevel(HttpLoggingInterceptor.Level.BASIC)
}