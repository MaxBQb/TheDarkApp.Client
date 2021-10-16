package lab.maxb.dark.Presentation.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import lab.maxb.dark.Presentation.Repository.Repository


class InputListViewModel : ViewModel() {
    var texts: MutableList<String> = mutableListOf("")

    fun getSuggestions(texts: List<String>): LiveData<Set<String>>
        = Repository.synonyms.getSynonyms(texts
            .map { it.trim() }
            .filterNot { it.isEmpty() }
            .toSet()
        )
}