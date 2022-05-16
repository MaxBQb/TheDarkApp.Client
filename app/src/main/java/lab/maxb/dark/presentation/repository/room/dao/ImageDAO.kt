package lab.maxb.dark.presentation.repository.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.presentation.repository.room.model.ImageDTO

@Dao
interface ImageDAO {
    @Insert(onConflict = REPLACE)
    suspend fun save(image: ImageDTO)

    @Query("SELECT * FROM image WHERE imageId = :id")
    fun getById(id: String): Flow<ImageDTO?>

    @Query("DELETE FROM image WHERE imageId = :id")
    suspend fun delete(id: String)
}