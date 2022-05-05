package lab.maxb.dark.presentation.repository.interfaces

interface SynonymsRepository {
    suspend fun getSynonyms(texts: Set<String>): Set<String>
}