import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.wearnn.viewModel.HealthViewModel
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wearnn.R
import com.example.wearnn.utils.AppColors
import com.example.wearnn.utils.AppFonts
import com.example.wearnn.utils.StatsNames

@Composable
fun MetricsScreen(healthViewModel: HealthViewModel) {
    val metrics = healthViewModel.configuredDailyData.collectAsState().value

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        metrics.forEach { metric ->
            StatRow(
                stat = metric,
                iconMap = mapOf(
                    StatsNames.move to R.drawable.ic_move,
                    StatsNames.exercise to R.drawable.ic_exercise,
                    StatsNames.stand to R.drawable.ic_stand,
                    StatsNames.steps to R.drawable.ic_steps,
                    StatsNames.distance to R.drawable.ic_distance,
                    StatsNames.climbed to R.drawable.ic_climb
                )
            )
        }
    }
}

@Composable
fun StatRow(stat: ActivityStat, iconMap: Map<String, Int>) {
    val iconId = iconMap[stat.title] ?: R.drawable.ic_move  // Default icon

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(50.dp),
            contentAlignment = Alignment.Center
        ) {
            // Draw the arc
            Canvas(modifier = Modifier.matchParentSize()) {
                stat.color?.let {
                    drawArc(
                        color = it.copy(alpha = 0.3f),
                        startAngle = -225f,
                        sweepAngle = 270f,
                        useCenter = false,
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                    )
                    stat.angle?.let { angle ->
                        drawArc(
                            color = it,
                            startAngle = -225f,
                            sweepAngle = angle,
                            useCenter = false,
                            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                }
            }

            // Overlay the icon
            Image(
                painter = painterResource(id = iconId),
                contentDescription = "${stat.title} icon",
                modifier = Modifier.size(24.dp)  // Adjusted for proper fit within the arc
            )
        }

        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = stat.title,
                color = AppColors.customGreyNonImportant,
                fontFamily = AppFonts.bebasNeueFont,
                style = TextStyle(fontSize = 18.sp)
            )
            Text(
                text = "${stat.progress}/${stat.goal} ${stat.unit}",
                color = Color.White,
                fontFamily = AppFonts.bebasNeueFont,
                style = TextStyle(fontSize = 14.sp)
            )
        }
    }
}