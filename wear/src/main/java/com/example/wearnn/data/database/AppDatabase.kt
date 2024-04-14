import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.wearnn.data.dao.HealthDataDao
import com.example.wearnn.data.model.HealthData
import com.example.wearnn.utils.LocalDateConverter

@Database(entities = [HealthData::class], version = 1)
@TypeConverters(LocalDateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun healthDataDao(): HealthDataDao
}
