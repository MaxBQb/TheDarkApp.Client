package lab.maxb.dark.Presentation.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import lab.maxb.dark.Presentation.Room.DAO.RecognitionTaskDAO
import lab.maxb.dark.Presentation.Room.Model.RecognitionTaskDTO

@Database(entities = [RecognitionTaskDTO::class],
          version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun recognitionTaskDao(): RecognitionTaskDAO

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getDatabase(context: Context): LocalDatabase
            = INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context)
            = Room.databaseBuilder(
                context.applicationContext,
                LocalDatabase::class.java, "dark_database"
            ).fallbackToDestructiveMigration().build()
    }
}