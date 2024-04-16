import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.wearnn.data.model.HealthStats



// this is just a demo to show how you can inflate xml's into Composable
@Composable
fun HealthStatsDisplay(healthStats: HealthStats) {
    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Today's Stats",
            style = MaterialTheme.typography.title1,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(text = "Steps: ${healthStats.steps}", style = MaterialTheme.typography.body1)
        Text(text = "Standing Time: ${healthStats.standingHours} minutes", style = MaterialTheme.typography.body1)
        Text(text = "Calories Burned: ${healthStats.caloriesBurned}", style = MaterialTheme.typography.body1)
    }
}

