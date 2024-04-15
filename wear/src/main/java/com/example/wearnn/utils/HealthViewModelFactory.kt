import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wearnn.data.dao.HealthDataDao

class HealthViewModelFactory(private val context: Context, private val healthDataDao: HealthDataDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HealthViewModelPreferences::class.java)) {
            return HealthViewModelPreferences(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
