import android.view.LayoutInflater
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.wearnn.data.model.HealthStats
import com.example.wearnn.databinding.ThirdscrenBinding


// this is just a demo to show how you can inflate xml's into Composable
@Composable
fun HealthStatsDisplay(healthStats: HealthStats) {
    val context = LocalContext.current

    AndroidView(
        factory = { ctx ->

            val inflater = LayoutInflater.from(ctx)
            val binding = ThirdscrenBinding.inflate(inflater)

            binding.tvTitle.text = "Today's Stats"
            binding.tvSteps.text = "Steps: ${healthStats.steps}"
            binding.tvStandingTime.text = "Standing Time: ${healthStats.standingMinutes} minutes"
            binding.tvCaloriesBurned.text = "Calories Burned: ${healthStats.caloriesBurned}"


            binding.root

        })
}
