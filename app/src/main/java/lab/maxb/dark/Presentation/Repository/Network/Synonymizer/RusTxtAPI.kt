package lab.maxb.dark.Presentation.Repository.Network.Synonymizer

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface RusTxtAPI {
    @POST(".")
    @Headers("Accept: application/json")
    fun getSynonym(
        @Body request: RequestBody,
    ): Call<SynonymFounder.SynonymResponse?>?
}
