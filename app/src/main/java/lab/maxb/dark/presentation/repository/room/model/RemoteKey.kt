package lab.maxb.dark.presentation.repository.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey
    override var id: String,
    val prevKey: Int?,
    val nextKey: Int?
): BaseLocalDTO()