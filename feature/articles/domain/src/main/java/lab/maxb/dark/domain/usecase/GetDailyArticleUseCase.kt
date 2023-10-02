package lab.maxb.dark.domain.usecase

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import lab.maxb.dark.domain.repository.ArticlesRepository
import org.koin.core.annotation.Singleton
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.random.Random

@Singleton
open class GetDailyArticleUseCase(
    private val articlesRepository: ArticlesRepository,
) {
    open operator fun invoke() = flow {
        while (true) {
            val articles = articlesRepository.getArticles().first()
            val seed = Instant.now().truncatedTo(ChronoUnit.DAYS).toEpochMilli()
            val pos = Random(seed).nextInt(0, articles.size)
            emit(articles[pos])
            val nextUpdate = Instant.now()
                .plus(1, ChronoUnit.DAYS)
                .truncatedTo(ChronoUnit.DAYS)
                .toEpochMilli()
            delay(nextUpdate - Instant.now().toEpochMilli())
        }
    }
}