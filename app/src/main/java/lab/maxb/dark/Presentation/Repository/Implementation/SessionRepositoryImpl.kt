package lab.maxb.dark.Presentation.Repository.Implementation

import lab.maxb.dark.Domain.Model.Server.Session
import lab.maxb.dark.Presentation.Repository.Interfaces.SessionRepository
import lab.maxb.dark.Presentation.Repository.Room.LocalDatabase
import lab.maxb.dark.Presentation.Repository.Room.Server.Model.SessionDTO

class SessionRepositoryImpl(db: LocalDatabase) : SessionRepository {
    private val mSessionDao = db.sessionDao()

    override suspend fun getSession(id: String, hash: String?, authCode: String?): Session?
        = if (authCode == null) mSessionDao.getSession(id, hash!!)?.toSession()
          else mSessionDao.getSessionByAuthCode(id, authCode)?.toSession()

    override suspend fun addSession(session: Session)
        = mSessionDao.addSession(SessionDTO(session))

    override suspend fun deleteSession(session: Session)
        = mSessionDao.deleteSession(SessionDTO(session))
}