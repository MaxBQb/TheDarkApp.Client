package lab.maxb.dark.data.datasource.remote

import okhttp3.MultipartBody

interface ImagesRemoteDataSource {
    suspend fun addImage(filePart: MultipartBody.Part): String?
    fun getImageSource(path: String): String
}

