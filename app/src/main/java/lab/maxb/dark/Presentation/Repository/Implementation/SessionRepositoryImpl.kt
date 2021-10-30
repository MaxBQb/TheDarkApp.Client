package lab.maxb.dark.Presentation.Repository.Implementation

import lab.maxb.dark.Domain.Model.Server.Session
import lab.maxb.dark.Presentation.Repository.Interfaces.SessionRepository
import lab.maxb.dark.Presentation.Repository.Room.LocalDatabase
import lab.maxb.dark.Presentation.Repository.Room.Server.Model.SessionDTO

class SessionRepositoryImpl(db: LocalDatabase) : SessionRepository {
    private val mSessionDao = db.sessionDao()

    override suspend fun getSession(id: String, hash: String): Session?
        = mSessionDao.getSession(id, hash)?.toSession()

    override suspend fun addSession(session: Session)
        = mSessionDao.addSession(SessionDTO(session))

    override suspend fun deleteSession(session: Session)
        = mSessionDao.deleteSession(SessionDTO(session))
}