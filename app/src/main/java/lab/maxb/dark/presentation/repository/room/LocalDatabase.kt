package lab.maxb.dark.presentation.repository.room

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import lab.maxb.dark.presentation.repository.room.Server.Model.ProfileDTO
import lab.maxb.dark.presentation.repository.room.dao.ImageDAO
import lab.maxb.dark.presentation.repository.room.dao.ProfileDAO
import lab.maxb.dark.presentation.repository.room.dao.RecognitionTaskDAO
import lab.maxb.dark.presentation.repository.room.dao.UserDAO
import lab.maxb.dark.presentation.repository.room.model.*

@Database(entities = [
            UserDTO::class,
            RecognitionTaskDTO::class,
            RecognitionTaskName::class,
            ImageDTO::class,
            RecognitionTaskImageCrossref::class,
            ProfileDTO::class,
          ], version = 4, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun recognitionTaskDao(): RecognitionTaskDAO
    abstract fun userDao(): UserDAO
    abstract fun profileDao(): ProfileDAO
    abstract fun imageDao(): ImageDAO

    companion object {
        internal fun build(app: Application)
            = Room.databaseBuilder(
                app.applicationContext,
                LocalDatabase::class.java, "dark_database"
            ).fallbackToDestructiveMigration()
            .build()
    }
}