package lab.maxb.dark.presentation.repository.interfaces

import com.bumptech.glide.load.model.GlideUrl
import kotlinx.coroutines.flow.Flow
import lab.maxb.dark.domain.model.Image

interface ImagesRepository {
    suspend fun save(image: Image)
    suspend fun getById(id: String): Flow<Image?>
    fun getUri(path: String): GlideUrl
    suspend fun delete(id: String)
}
