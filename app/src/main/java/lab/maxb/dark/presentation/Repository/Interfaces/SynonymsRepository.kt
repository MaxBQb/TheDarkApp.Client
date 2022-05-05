package lab.maxb.dark.presentation.Repository.Interfaces

interface SynonymsRepository {
    suspend fun getSynonyms(texts: Set<String>): Set<String>
}