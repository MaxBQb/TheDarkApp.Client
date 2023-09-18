package lab.maxb.dark.data.datasource

import okhttp3.MultipartBody

interface ImagesRemoteDataSource {
    suspend fun addImage(filePart: MultipartBody.Part): String?
    fun getImageSource(path: String): String
}

