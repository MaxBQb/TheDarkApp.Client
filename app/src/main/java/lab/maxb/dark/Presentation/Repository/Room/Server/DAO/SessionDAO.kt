package lab.maxb.dark.Presentation.Repository.Room.Server.DAO

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import lab.maxb.dark.Presentation.Repository.Room.Server.Model.SessionDTO
import lab.maxb.dark.Presentation.Repository.Room.Server.Relation.SessionWithProfile

@Dao
interface SessionDAO {
    @Insert(onConflict = IGNORE)
    suspend fun addSession(session: SessionDTO)

    @Delete
    suspend fun deleteSession(session: SessionDTO)

    @Transaction
    @Query("""
        SELECT * FROM session 
        WHERE id = :id 
        AND hash = :hash 
        AND expires >= strftime('%s','now')
    """)
    suspend fun getSession(id: String, hash: String): SessionWithProfile?
}