package lab.maxb.dark.domain.usecase.task

import kotlinx.coroutines.flow.first
import lab.maxb.dark.domain.repository.SynonymsRepository
import lab.maxb.dark.domain.usecase.settings.UseExternalSuggestionsUseCases
import org.koin.core.annotation.Singleton

@Singleton
open class GetTaskNameSynonymsUseCase(
    private val synonymsRepository: SynonymsRepository,
    private val useExternalSuggestionsUseCases: UseExternalSuggestionsUseCases,
) {
    open suspend operator fun invoke(names: List<String>) =
        if (useExternalSuggestionsUseCases.get().first())
            synonymsRepository.getSynonyms(names.toSet())
                .map { it.trim() }
                .filterNot { it.isEmpty() }
                .toSet()
        else emptySet()
}