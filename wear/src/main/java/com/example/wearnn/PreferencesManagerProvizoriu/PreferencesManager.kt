import android.content.Context
import android.content.SharedPreferences
import java.time.LocalDate

object PreferencesManager {
    private const val PREF_NAME = "wearnn_preferences"
    private const val KEY_PROGRESS = "progress"
    private const val KEY_GOAL = "goal"
    private const val KEY_DATE = "date"
    private const val KEY_TYPE = "type"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveHealthData(context: Context, progress: Int, goal: Int, date: LocalDate, type: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putInt(KEY_PROGRESS, progress)
        editor.putInt(KEY_GOAL, goal)
        editor.putString(KEY_DATE, date.toString())
        editor.putString(KEY_TYPE, type)
        editor.apply()
    }

    fun getHealthData(context: Context): HealthDataPreferences {
        val sharedPreferences = getSharedPreferences(context)
        val progress = sharedPreferences.getInt(KEY_PROGRESS, 0)
        val goal = sharedPreferences.getInt(KEY_GOAL, 0)
        val dateString = sharedPreferences.getString(KEY_DATE, "") ?: ""
        val date = if (dateString.isNotEmpty()) LocalDate.parse(dateString) else LocalDate.now()
        val type = sharedPreferences.getString(KEY_TYPE, "") ?: ""
        return HealthDataPreferences(progress, goal, date, type)
    }
}
