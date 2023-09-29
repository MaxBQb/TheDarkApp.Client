package lab.maxb.dark.ui.model

import lab.maxb.dark.domain.model.Article

data class ArticleListItem(
    val title: String,
    val body: String,
    val id: String,
)

fun Article.toPresentation() = ArticleListItem(
    title = title,
    body = body,
    id = id,
)
