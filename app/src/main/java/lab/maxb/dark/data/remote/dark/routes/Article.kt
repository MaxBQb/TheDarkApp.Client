package lab.maxb.dark.data.remote.dark.routes

import lab.maxb.dark.data.model.remote.ArticleCreationNetworkDTO
import lab.maxb.dark.data.model.remote.ArticleNetworkDTO
import retrofit2.http.*

interface Article {
    @GET("$path/")
    suspend fun getAllArticles(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 2,
    ): List<ArticleNetworkDTO>?

    @PUT("$path/{id}")
    suspend fun updateArticle(
        @Path("id") id: String,
        @Body article: ArticleCreationNetworkDTO,
    ): ArticleNetworkDTO?

    @POST("$path/")
    suspend fun addArticle(@Body article: ArticleCreationNetworkDTO): ArticleNetworkDTO?

    companion object {
        const val path = "/articles"
    }
}
