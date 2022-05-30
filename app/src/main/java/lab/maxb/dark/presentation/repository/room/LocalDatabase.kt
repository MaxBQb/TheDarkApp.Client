package lab.maxb.dark.presentation.repository.room

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import lab.maxb.dark.presentation.repository.room.converters.CollectionsConverter
import lab.maxb.dark.presentation.repository.room.dao.ProfilesDAO
import lab.maxb.dark.presentation.repository.room.dao.RecognitionTasksDAO
import lab.maxb.dark.presentation.repository.room.dao.RemoteKeysDAO
import lab.maxb.dark.presentation.repository.room.dao.UsersDAO
import lab.maxb.dark.presentation.repository.room.model.ProfileLocalDTO
import lab.maxb.dark.presentation.repository.room.model.RecognitionTaskLocalDTO
import lab.maxb.dark.presentation.repository.room.model.RemoteKey
import lab.maxb.dark.presentation.repository.room.model.UserLocalDTO

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