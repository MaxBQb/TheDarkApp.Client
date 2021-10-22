package lab.maxb.dark.Presentation.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import lab.maxb.dark.Presentation.Repository.Interfaces.ISynonymsRepository


class InputListViewModel(
    private val synonymsRepository: ISynonymsRepository,
) : ViewModel() {
    var texts: MutableList<String> = mutableListOf("")

    fun getSuggestions(texts: List<String>): LiveData<Set<String>>
        = synonymsRepository.getSynonyms(texts
            .map { it.trim() }
            .filterNot { it.isEmpty() }
            .toSet()
        )
}