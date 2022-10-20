package lab.maxb.dark.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey
    override val id: String,
    val prevKey: Int?,
    val nextKey: Int?
): BaseLocalDTO()