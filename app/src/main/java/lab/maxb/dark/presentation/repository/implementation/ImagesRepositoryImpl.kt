package lab.maxb.dark.presentation.repository.implementation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import lab.maxb.dark.domain.model.Image
import lab.maxb.dark.presentation.extra.ImageLoader
import lab.maxb.dark.presentation.repository.interfaces.ImagesRepository
import lab.maxb.dark.presentation.repository.network.dark.DarkService
import lab.maxb.dark.presentation.repository.room.LocalDatabase
import lab.maxb.dark.presentation.repository.room.model.toImage
import lab.maxb.dark.presentation.repository.room.model.toImageDTO
import lab.maxb.dark.presentation.repository.utils.StaticResource
import org.koin.core.annotation.Single

@Single
class ImagesRepositoryImpl(
    db: LocalDatabase,
    private val mDarkService: DarkService,
    private val imageLoader: ImageLoader,
) : ImagesRepository {
    private val mImageDao = db.imageDao()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val imageResource = StaticResource<String, Image>().apply {
        fetchRemote = {
            Image(it, it)
        }
        localStore = {
            mImageDao.save(it.toImageDTO())
        }
        clearLocalStore = {
            mImageDao.delete(it)
        }
        fetchLocal = {
            mImageDao.getById(it).mapLatest { image ->
                image?.toImage()
            }
        }
    }

    override suspend fun save(image: Image) = mImageDao.save(image.toImageDTO())

    override suspend fun getById(id: String) = imageResource.query(id)

    override fun getUri(path: String)
        = mDarkService.getImageSource(path)

    override suspend fun delete(id: String) = mImageDao.delete(id)
}