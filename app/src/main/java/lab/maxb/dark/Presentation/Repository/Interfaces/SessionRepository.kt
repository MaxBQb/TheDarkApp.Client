package lab.maxb.dark.Presentation.Repository.Interfaces

import lab.maxb.dark.Domain.Model.Server.Session

interface SessionRepository {
    suspend fun getSession(id: String, hash: String): Session?

    suspend fun addSession(session: Session)

    suspend fun deleteSession(session: Session)
}