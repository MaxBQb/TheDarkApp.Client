package lab.maxb.dark.domain.usecase.task

import lab.maxb.dark.domain.repository.SynonymsRepository
import org.koin.core.annotation.Singleton

@Singleton
open class GetTaskNameSynonymsUseCase(
    private val synonymsRepository: SynonymsRepository,
) {
    open suspend operator fun invoke(names: List<String>)
        = synonymsRepository.getSynonyms(names.toSet())
            .map { it.trim() }
            .filterNot { it.isEmpty() }
            .toSet()
}