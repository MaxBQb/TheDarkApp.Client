package lab.maxb.dark.Presentation.Repository.Interfaces

import androidx.lifecycle.LiveData

interface SynonymsRepository {
    fun getSynonyms(texts: Set<String>): LiveData<Set<String>>
}