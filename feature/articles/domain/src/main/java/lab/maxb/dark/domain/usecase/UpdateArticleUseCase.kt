package lab.maxb.dark.domain.usecase

import kotlinx.coroutines.flow.firstOrNull
import lab.maxb.dark.domain.model.createArticle
import lab.maxb.dark.domain.repository.ArticlesRepository
import org.koin.core.annotation.Singleton

@Singleton
open class UpdateArticleUseCase(
    private val articlesRepository: ArticlesRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) {
    open suspend operator fun invoke(
        title: String,
        body: String,
        id: String,
    ) {
        val article = createArticle(
            title,
            body,
            getCurrentUserUseCase().firstOrNull()!!.id,
        )!!.copy(id=id)
        articlesRepository.updateArticle(article)
    }
}