package lab.maxb.dark.data.remote.synonymizer

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface RusTxtAPI {
    @POST(".")
    @Headers("Accept: application/json")
    suspend fun getSynonym(
        @Body request: RequestBody,
    ): SynonymFounder.SynonymResponse?
}
