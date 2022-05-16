package lab.maxb.dark.presentation.repository.room

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import lab.maxb.dark.presentation.repository.room.Server.Model.ProfileDTO
import lab.maxb.dark.presentation.repository.room.dao.*
import lab.maxb.dark.presentation.repository.room.model.*

@Database(entities = [
            UserDTO::class,
            RecognitionTaskDTO::class,
            RecognitionTaskName::class,
            ImageDTO::class,
            RecognitionTaskImageCrossref::class,
            ProfileDTO::class,
            RemoteKeys::class,
          ], version = 5, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun recognitionTaskDao(): RecognitionTaskDAO
    abstract fun userDao(): UserDAO
    abstract fun profileDao(): ProfileDAO
    abstract fun imageDao(): ImageDAO
    abstract fun remoteKeysDao(): RemoteKeysDAO

    companion object {
        internal fun build(app: Application)
            = Room.databaseBuilder(
                app.applicationContext,
                LocalDatabase::class.java, "dark_database"
            ).fallbackToDestructiveMigration()
            .build()
    }
}