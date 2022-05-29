package lab.maxb.dark.presentation.repository.room

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import lab.maxb.dark.presentation.repository.room.Server.Model.ProfileDTO
import lab.maxb.dark.presentation.repository.room.converters.ListConverter
import lab.maxb.dark.presentation.repository.room.dao.ProfileDAO
import lab.maxb.dark.presentation.repository.room.dao.RecognitionTaskDAO
import lab.maxb.dark.presentation.repository.room.dao.RemoteKeysDAO
import lab.maxb.dark.presentation.repository.room.dao.UserDAO
import lab.maxb.dark.presentation.repository.room.model.RecognitionTaskDTO
import lab.maxb.dark.presentation.repository.room.model.RecognitionTaskName
import lab.maxb.dark.presentation.repository.room.model.RemoteKeys
import lab.maxb.dark.presentation.repository.room.model.UserDTO

@Database(entities = [
            UserDTO::class,
            RecognitionTaskDTO::class,
            RecognitionTaskName::class,
            ProfileDTO::class,
            RemoteKeys::class,
          ], version = 6, exportSchema = false)
@TypeConverters(ListConverter::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun recognitionTaskDao(): RecognitionTaskDAO
    abstract fun userDao(): UserDAO
    abstract fun profileDao(): ProfileDAO
    abstract fun remoteKeysDao(): RemoteKeysDAO

    companion object {
        internal fun build(app: Application) = Room.databaseBuilder(
            app.applicationContext,
            LocalDatabase::class.java, "dark_database"
        ).fallbackToDestructiveMigration().build()
    }
}