
import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Room
import com.example.wearnn.data.dao.HealthDataDao
import com.example.wearnn.data.model.HealthData

@Database(entities = [HealthData::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun healthDataDao(): HealthDataDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Correct the function signature to return AppDatabase
        fun getDatabase(context: Context): AppDatabase {
            Log.d("DatabaseInit", "Getting database instance...")
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                Log.d("DatabaseInit", "Returning existing instance")
                return tempInstance
            }
            synchronized(this) {
                Log.d("DatabaseInit", "Creating new database instance")
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "your_database_name"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                Log.d("DatabaseInit", "Database instance created successfully")
                return instance
            }
        }

    }
}
