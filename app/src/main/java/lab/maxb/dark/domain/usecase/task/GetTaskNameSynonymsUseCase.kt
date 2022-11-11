package lab.maxb.dark.domain.usecase.task

import lab.maxb.dark.domain.repository.SynonymsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class GetTaskNameSynonymsUseCase @Inject constructor(
    private val synonymsRepository: SynonymsRepository,
) {
    open suspend operator fun invoke(names: List<String>)
        = synonymsRepository.getSynonyms(names.toSet())
            .map { it.trim() }
            .filterNot { it.isEmpty() }
            .toSet()
}