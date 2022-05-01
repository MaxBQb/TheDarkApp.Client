package lab.maxb.dark.Presentation.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import lab.maxb.dark.Presentation.Repository.Interfaces.SynonymsRepository
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class InputListViewModel(
    private val synonymsRepository: SynonymsRepository,
) : ViewModel() {
    var texts: MutableList<String> = mutableListOf("")

    fun getSuggestions(texts: List<String>): LiveData<Set<String>>
        = liveData(viewModelScope.coroutineContext) {
            emit(synonymsRepository.getSynonyms(texts.toSet())
                .map { it.trim() }
                .filterNot { it.isEmpty() }
                .toSet())
        }
}