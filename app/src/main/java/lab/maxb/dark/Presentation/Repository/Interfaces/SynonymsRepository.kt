package lab.maxb.dark.Presentation.Repository.Interfaces

interface SynonymsRepository {
    suspend fun getSynonyms(texts: Set<String>): Set<String>
}