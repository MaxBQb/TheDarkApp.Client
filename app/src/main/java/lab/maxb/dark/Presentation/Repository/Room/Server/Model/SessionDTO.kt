package lab.maxb.dark.Presentation.Repository.Room.Server.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import lab.maxb.dark.Domain.Model.Server.Session

@Entity(tableName = "session", ignoredColumns = ["profile"])
data class SessionDTO(
    @PrimaryKey
    override var id: String,
    var profile_id: String,
    override var expires: Long,
    override var hash: String,
): Session(null, expires, hash) {
    constructor(session: Session) : this(
        session.id,
        session.profile!!.id,
        session.expires,
        session.hash
    )
}
