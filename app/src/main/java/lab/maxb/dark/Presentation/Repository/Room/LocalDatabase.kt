package lab.maxb.dark.Presentation.Repository.Room

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import lab.maxb.dark.Presentation.Repository.Room.DAO.RecognitionTaskDAO
import lab.maxb.dark.Presentation.Repository.Room.DAO.UserDAO
import lab.maxb.dark.Presentation.Repository.Room.Model.RecognitionTaskDTO
import lab.maxb.dark.Presentation.Repository.Room.Model.RecognitionTaskImage
import lab.maxb.dark.Presentation.Repository.Room.Model.RecognitionTaskName
import lab.maxb.dark.Presentation.Repository.Room.Model.UserDTO
import lab.maxb.dark.Presentation.Repository.Room.Server.DAO.ProfileDAO
import lab.maxb.dark.Presentation.Repository.Room.Server.DAO.SessionDAO
import lab.maxb.dark.Presentation.Repository.Room.Server.Model.ProfileDTO
import lab.maxb.dark.Presentation.Repository.Room.Server.Model.SessionDTO

@Database(entities = [
            UserDTO::class,
            RecognitionTaskDTO::class,
            RecognitionTaskName::class,
            RecognitionTaskImage::class,

            // Server side
            SessionDTO::class,
            ProfileDTO::class,
          ], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun recognitionTaskDao(): RecognitionTaskDAO
    abstract fun userDao(): UserDAO
    abstract fun profileDao(): ProfileDAO
    abstract fun sessionDao(): SessionDAO

    companion object {
        fun buildDatabase(app: Application)
            = Room.databaseBuilder(
                app.applicationContext,
                LocalDatabase::class.java, "dark_database"
            ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
}