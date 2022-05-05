package lab.maxb.dark.presentation.Repository.Room

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import lab.maxb.dark.presentation.Repository.Room.DAO.ProfileDAO
import lab.maxb.dark.presentation.Repository.Room.DAO.RecognitionTaskDAO
import lab.maxb.dark.presentation.Repository.Room.DAO.UserDAO
import lab.maxb.dark.presentation.Repository.Room.Model.RecognitionTaskDTO
import lab.maxb.dark.presentation.Repository.Room.Model.RecognitionTaskImage
import lab.maxb.dark.presentation.Repository.Room.Model.RecognitionTaskName
import lab.maxb.dark.presentation.Repository.Room.Model.UserDTO
import lab.maxb.dark.presentation.Repository.Room.Server.Model.ProfileDTO

@Database(entities = [
            UserDTO::class,
            RecognitionTaskDTO::class,
            RecognitionTaskName::class,
            RecognitionTaskImage::class,
            ProfileDTO::class,
          ], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun recognitionTaskDao(): RecognitionTaskDAO
    abstract fun userDao(): UserDAO
    abstract fun profileDao(): ProfileDAO

    companion object {
        internal fun build(app: Application)
            = Room.databaseBuilder(
                app.applicationContext,
                LocalDatabase::class.java, "dark_database"
            ).fallbackToDestructiveMigration()
            .build()
    }
}