package lab.maxb.dark.data.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import lab.maxb.dark.data.converters.CollectionsConverter
import lab.maxb.dark.data.local.dao.ArticlesDAO
import lab.maxb.dark.data.local.dao.RecognitionTasksDAO
import lab.maxb.dark.data.local.dao.RemoteKeysDAO
import lab.maxb.dark.data.local.dao.UsersDAO
import lab.maxb.dark.data.local.model.ArticleLocalDTO
import lab.maxb.dark.data.local.model.RecognitionTaskLocalDTO
import lab.maxb.dark.data.local.model.RemoteKey
import lab.maxb.dark.data.local.model.UserLocalDTO
import lab.maxb.dark.domain.repository.LocalStorage
import org.koin.core.annotation.Singleton

@Database(
    entities = [
        UserLocalDTO::class,
        RecognitionTaskLocalDTO::class,
        ArticleLocalDTO::class,
        RemoteKey::class,
    ],
    version = 12,
    exportSchema = false,
)
@TypeConverters(CollectionsConverter::class)
abstract class LocalDatabase : RoomDatabase(), LocalStorage {
    abstract fun articles(): ArticlesDAO
    abstract fun recognitionTasks(): RecognitionTasksDAO
    abstract fun users(): UsersDAO
    abstract fun remoteKeys(): RemoteKeysDAO

    override suspend fun clear() {
        clearAllTables()
    }
}

@Singleton([LocalStorage::class, RoomDatabase::class, LocalDatabase::class])
internal fun buildLocalDatabase(app: Application) = Room.databaseBuilder(
    app.applicationContext,
    LocalDatabase::class.java, "dark_database"
).fallbackToDestructiveMigration().build()
