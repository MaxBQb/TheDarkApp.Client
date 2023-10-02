package lab.maxb.dark.data.local.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import lab.maxb.dark.data.local.model.BaseLocalDTO

interface BaseDAO<DTO: BaseLocalDTO> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(value: DTO): Long

    @Update
    suspend fun update(value: DTO)

    @Delete
    suspend fun delete(vararg value: DTO)
}