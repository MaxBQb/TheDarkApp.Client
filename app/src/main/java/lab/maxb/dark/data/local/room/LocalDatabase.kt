package lab.maxb.dark.data.local.room

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import lab.maxb.dark.data.local.room.converters.CollectionsConverter
import lab.maxb.dark.data.local.room.dao.ArticlesDAO
import lab.maxb.dark.data.local.room.dao.RecognitionTasksDAO
import lab.maxb.dark.data.local.room.dao.RemoteKeysDAO
import lab.maxb.dark.data.local.room.dao.UsersDAO
import lab.maxb.dark.data.model.local.ArticleLocalDTO
import lab.maxb.dark.data.model.local.RecognitionTaskLocalDTO
import lab.maxb.dark.data.model.local.RemoteKey
import lab.maxb.dark.data.model.local.UserLocalDTO
import lab.maxb.dark.domain.repository.LocalStorage

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

    companion object {
        internal fun build(app: Application) = Room.databaseBuilder(
            app.applicationContext,
            LocalDatabase::class.java, "dark_database"
        ).fallbackToDestructiveMigration().build()
    }

    override suspend fun clear() {
        clearAllTables()
    }
}