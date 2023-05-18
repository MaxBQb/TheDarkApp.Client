package lab.maxb.dark.domain.usecase.article

import lab.maxb.dark.domain.repository.ArticlesRepository
import org.koin.core.annotation.Singleton

@Singleton
open class GetArticlesListUseCase(
    private val articlesRepository: ArticlesRepository,
) {
    open operator fun invoke() = articlesRepository.getAllArticles()
}