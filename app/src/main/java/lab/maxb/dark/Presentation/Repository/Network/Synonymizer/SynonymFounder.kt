package lab.maxb.dark.Presentation.Repository.Network.Synonymizer

import lab.maxb.dark.Presentation.Repository.Interfaces.SynonymsRepository
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SynonymFounder : SynonymsRepository {
    private val api: RusTxtAPI
    private suspend fun getSynonym(text: String): String? {
        return api.getSynonym(MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("method","getSynText")
            .addFormDataPart("text", text)
            .build()
        )?.modified_text?.let { fixText(it) }
    }

    private fun fixText(text: String)
        = text.lowercase().trim()
        .removeSuffix("<br>").trim()
        .replace(Regex("\\s+"), " ")

    override suspend fun getSynonyms(texts: Set<String>): Set<String> {
        val fixedTexts = texts.map { fixText(it) }.toSet()
        val results = mutableSetOf<String>()

        fixedTexts.forEach { text ->
            getSynonym(text)?.let {
                if (it.isNotBlank() && it !in fixedTexts)
                    results.add(it)
            }
        }
        return results
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
