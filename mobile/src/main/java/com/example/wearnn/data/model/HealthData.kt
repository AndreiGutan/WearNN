import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "health_data")
data class HealthData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,
    val progress: Int,
    val goal: Int,
    val date: Date,  // Using Date here for simplicity, consider using a Long timestamp for precision
    val extendedInfo: String? = null  // Additional field for extra data
)
