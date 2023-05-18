package lab.maxb.dark.domain.model

import lab.maxb.dark.domain.operations.randomUUID
import java.util.Locale

data class Article(
    val title: String,
    val body: String,
    val authorId: String,
    val id: String = randomUUID,
) {
    companion object {
        const val MAX_TITLE_LENGTH = 60
        const val MIN_TITLE_LENGTH = 3
        const val MIN_BODY_LENGTH = 3
        const val MAX_BODY_LENGTH = 5000
    }
}

fun createArticle(
    title: String,
    body: String,
    authorId: String,
): Article? {
    val trimmedTitle = title.trim()
    val trimmedBody = body.trim()
    if (trimmedTitle.length !in Article.MIN_TITLE_LENGTH..Article.MAX_TITLE_LENGTH)
        return null
    if (trimmedBody.length !in Article.MIN_BODY_LENGTH..Article.MAX_BODY_LENGTH)
        return null
    return Article(
        trimmedTitle.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
        trimmedBody,
        authorId,
    )
}