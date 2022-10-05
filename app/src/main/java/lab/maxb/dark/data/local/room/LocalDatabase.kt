package lab.maxb.dark.data.local.room

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import lab.maxb.dark.data.local.room.converters.CollectionsConverter
import lab.maxb.dark.data.local.room.dao.ProfilesDAO
import lab.maxb.dark.data.local.room.dao.RecognitionTasksDAO
import lab.maxb.dark.data.local.room.dao.RemoteKeysDAO
import lab.maxb.dark.data.local.room.dao.UsersDAO
import lab.maxb.dark.data.model.local.ProfileLocalDTO
import lab.maxb.dark.data.model.local.RecognitionTaskLocalDTO
import lab.maxb.dark.data.model.local.RemoteKey
import lab.maxb.dark.data.model.local.UserLocalDTO

@Database(entities = [
            UserLocalDTO::class,
            RecognitionTaskLocalDTO::class,
            ProfileLocalDTO::class,
            RemoteKey::class,
          ], version = 7, exportSchema = false)
@TypeConverters(CollectionsConverter::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun recognitionTasks(): RecognitionTasksDAO
    abstract fun users(): UsersDAO
    abstract fun profiles(): ProfilesDAO
    abstract fun remoteKeys(): RemoteKeysDAO

    companion object {
        internal fun build(app: Application) = Room.databaseBuilder(
            app.applicationContext,
            LocalDatabase::class.java, "dark_database"
        ).fallbackToDestructiveMigration().build()
    }
}