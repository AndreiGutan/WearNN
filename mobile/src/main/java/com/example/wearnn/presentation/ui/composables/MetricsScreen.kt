import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wearnn.R
import com.example.wearnn.utils.AppColors
import com.example.wearnn.utils.AppFonts
import com.example.wearnn.utils.StatsNames
import com.example.wearnn.viewModel.HealthViewModel

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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 3.dp)
            .background(AppColors.customDarkGrey, RoundedCornerShape(16.dp))  // Rounded corners and blue background
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(80.dp)
                    .offset(x = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                // Draw the arc
                Canvas(modifier = Modifier.matchParentSize()
                    .offset(y=4.dp)) {
                    stat.color?.let {
                        drawArc(
                            color = it.copy(alpha = 0.3f),
                            startAngle = -225f,
                            sweepAngle = 270f,
                            useCenter = false,
                            style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                        )
                        stat.angle?.let { angle ->
                            drawArc(
                                color = it,
                                startAngle = -225f,
                                sweepAngle = angle,
                                useCenter = false,
                                style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }
                    }
                }

                // Overlay the icon
                Image(
                    painter = painterResource(id = iconId),
                    contentDescription = "${stat.title} icon",
                    modifier = Modifier.size(55.dp)
                )
            }

            Column(modifier = Modifier.padding(start = 35.dp)) {
                Text(
                    text = stat.title,
                    color = Color.White,
                    fontFamily = AppFonts.bebasNeueFont,
                    style = TextStyle(fontSize = 35.sp)
                )
                Text(
                    text = "${stat.progress} / ${stat.goal} ${stat.unit}",
                    color = Color.White,
                    fontFamily = AppFonts.bebasNeueFont,
                    style = TextStyle(fontSize = 28.sp)
                )
            }
        }
    }
}

