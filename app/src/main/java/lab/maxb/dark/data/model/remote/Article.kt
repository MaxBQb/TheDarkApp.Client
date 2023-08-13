package lab.maxb.dark.data.model.remote

import lab.maxb.dark.domain.model.Article
import lab.maxb.dark.domain.model.modelRefOf


class ArticleNetworkDTO (
    val title: String,
    val body: String,
    val authorId: String,
    val id: String,
)

class ArticleCreationNetworkDTO(
    val title: String,
    val body: String,
)

fun Article.toNetworkDTO() = ArticleCreationNetworkDTO(
    title,
    body,
)

fun ArticleNetworkDTO.toDomain() = Article(
    title,
    body,
    modelRefOf(authorId),
    id,
)
