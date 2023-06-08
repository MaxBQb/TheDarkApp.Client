package lab.maxb.dark.domain.usecase.article

import kotlinx.coroutines.flow.firstOrNull
import lab.maxb.dark.domain.model.createArticle
import lab.maxb.dark.domain.repository.ArticlesRepository
import lab.maxb.dark.domain.usecase.user.GetCurrentUserUseCase
import org.koin.core.annotation.Singleton

@Singleton
open class CreateArticleUseCase(
    private val articlesRepository: ArticlesRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) {
    open suspend operator fun invoke(
        title: String,
        body: String,
    ) {
        val article = createArticle(
            title,
            body,
            getCurrentUserUseCase().firstOrNull()!!.id,
        )!!
        articlesRepository.addArticle(article)
    }
}