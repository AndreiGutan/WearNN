import androidx.compose.ui.graphics.Color

// Modify ActivityStat to include an angle calculation based on progress and goal
data class ActivityStat(val title: String, val unit: String, val progress: Int, val goal: Int, val color: Color) {
    val angle: Float get() = (270f * progress / goal).coerceIn(0f, 270f) // This ensures the angle is within 0 to 270 degrees
}