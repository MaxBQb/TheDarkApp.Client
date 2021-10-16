package lab.maxb.dark.Presentation.Repository.Network.Synonymizer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import lab.maxb.dark.Presentation.Repository.Interfaces.ISynonymsRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.MultipartBody


class SynonymFounder : ISynonymsRepository {
    private val api: RusTxtAPI
    private fun getSynonym(text: String): LiveData<String?> {
        val addresses = MutableLiveData<String?>()
        api.getSynonym(MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("method","getSynText")
            .addFormDataPart("text", text)
            .build()
        )?.enqueue(object : Callback<SynonymResponse?> {
            override fun onResponse(
                call: Call<SynonymResponse?>,
                response: Response<SynonymResponse?>
            ) {
                if (response.isSuccessful)
                    addresses.postValue(response.body()?.modified_text?: return)
            }

            override fun onFailure(call: Call<SynonymResponse?>, t: Throwable) {
                t.message
            }
        })
        return addresses
    }

    override fun getSynonyms(texts: Set<String>): LiveData<Set<String>> {
        val mediator = MediatorLiveData<Set<String>>()
        val results = mutableSetOf<String>()
        mediator.value = results
        texts.forEach { text ->
            mediator.addSource(getSynonym(text)) { it?.let {
                results.add(it)
                mediator.value = results
            }}
        }
        return mediator
    }

    class SynonymResponse {
        var modified_text: String? = null
        // I'm not interested in other parts of response
    }

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://rustxt.ru/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(RusTxtAPI::class.java)
    }
}
