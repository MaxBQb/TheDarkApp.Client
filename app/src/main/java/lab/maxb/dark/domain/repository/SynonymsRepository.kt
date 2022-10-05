package lab.maxb.dark.domain.repository

interface SynonymsRepository {
    suspend fun getSynonyms(texts: Set<String>): Set<String>
}