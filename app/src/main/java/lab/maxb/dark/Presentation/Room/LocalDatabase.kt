package lab.maxb.dark.Presentation.Room

import android.content.Context
import androidx.room.*
import lab.maxb.dark.Presentation.Room.Model.RecognitionTaskDTO
import lab.maxb.dark.Presentation.Room.DAO.RecognitionTaskDAO
import kotlin.jvm.Volatile
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(entities = [RecognitionTaskDTO::class],
          version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun recognitionTaskDao(): RecognitionTaskDAO

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null
        private const val NUMBER_OF_THREADS = 4
        val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)
        fun getDatabase(context: Context): LocalDatabase? {
            if (INSTANCE == null) {
                synchronized(LocalDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            LocalDatabase::class.java, "dark_database"
                        ).fallbackToDestructiveMigration()
                         .allowMainThreadQueries()
                         .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}