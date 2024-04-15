import java.time.LocalDate

data class HealthDataPreferences(
    val progress: Int,
    val goal: Int,
    val date: LocalDate,
    val type: String
)
