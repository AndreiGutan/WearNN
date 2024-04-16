import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.Date

@Dao
interface HealthDataDao {
    @Insert
    suspend fun insert(healthData: HealthData)

    @Query("SELECT * FROM health_data WHERE date BETWEEN :start AND :end")
    suspend fun loadDataBetweenDates(start: Date, end: Date): List<HealthData>
}
