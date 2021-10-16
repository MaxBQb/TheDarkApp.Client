package lab.maxb.dark.Presentation.Repository.Interfaces

import androidx.lifecycle.LiveData

interface ISynonymsRepository {
    fun getSynonyms(texts: Set<String>): LiveData<Set<String>>
}