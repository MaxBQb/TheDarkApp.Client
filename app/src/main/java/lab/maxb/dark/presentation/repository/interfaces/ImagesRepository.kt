package lab.maxb.dark.presentation.repository.interfaces

import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.domain.model.Image

interface ImagesRepository {
    suspend fun save(image: Image)
    suspend fun getById(id: String): Flow<Image?>
    suspend fun delete(id: String)
}
