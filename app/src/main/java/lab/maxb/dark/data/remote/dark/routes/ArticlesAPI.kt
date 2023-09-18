package lab.maxb.dark.data.remote.dark.routes

import lab.maxb.dark.data.datasource.ArticlesRemoteDataSource
import lab.maxb.dark.data.model.remote.ArticleCreationNetworkDTO
import lab.maxb.dark.data.model.remote.ArticleNetworkDTO
import retrofit2.http.*

interface ArticlesAPI : ArticlesRemoteDataSource {
    @GET("$path/")
    override suspend fun getAllArticles(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): List<ArticleNetworkDTO>?

    @PUT("$path/{id}")
    override suspend fun updateArticle(
        @Path("id") id: String,
        @Body article: ArticleCreationNetworkDTO,
    ): ArticleNetworkDTO?

    @POST("$path/")
    override suspend fun addArticle(@Body article: ArticleCreationNetworkDTO): ArticleNetworkDTO?

    companion object {
        const val path = "/articles"
    }
}
