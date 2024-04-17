package com.example.wearnn.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.wear.compose.material.Text
import com.example.wearnn.R

import com.example.wearnn.utils.AppColors
import com.example.wearnn.utils.AppFonts
import com.example.wearnn.utils.HealthViewModelFactory
import com.example.wearnn.utils.StatsNames
import com.example.wearnn.viewModel.HealthViewModel
import com.example.wearnn.data.database.AppDatabase
import com.example.wearnn.utils.ActivityStat


open class DashboardActivity : AppCompatActivity() {
    private lateinit var viewModel: HealthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Dashboard1lMobile", "aa1")
        // Assuming you have a method to get your database instance and subsequently the DAO
        val healthDataDao = AppDatabase.getDatabase(this).healthDataDao()

        // Using the ViewModelFactory to create an instance of HealthViewModel
        viewModel = ViewModelProvider(this, HealthViewModelFactory(healthDataDao)).get(HealthViewModel::class.java)

        setContent {
            HealthStatsScreen(viewModel)
        }
    }
}



@Composable
fun HealthStatsScreen(viewModel: HealthViewModel) {
    val activityStats = viewModel.configuredDailyData.collectAsState().value

    LaunchedEffect(key1 = activityStats) {
        Log.d("DashboardActivity", "Recomposing with stats: $activityStats")
    }

    if (activityStats.isEmpty()) {
        Text("No data available")
    } else {
        activityStats.forEach { stat ->
            ActivityStatRow(stat)
        }
    }
}



@Composable
fun ActivityStatRow(activityStat: ActivityStat) {
    val iconId = when (activityStat.title) {
        StatsNames.move -> R.drawable.ic_move
        StatsNames.exercise -> R.drawable.ic_exercise
        StatsNames.stand -> R.drawable.ic_stand
        else -> null
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .padding(4.dp)
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawArc(
                    color = Color.LightGray,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                )
                drawArc(
                    color = getColorForStat(activityStat.title),
                    startAngle = -90f,
                    sweepAngle = (360f * activityStat.progress / activityStat.goal!!).toFloat(),
                    useCenter = false,
                    style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            iconId?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = "${activityStat.title} icon",
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.Center)
                )
            }
        }
        Column(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(
                text = "${activityStat.title}: ${activityStat.progress} ${activityStat.unit}",
                style = TextStyle(color = Color.White, fontSize = 20.sp)
            )
            Text(
                text = "Goal: ${activityStat.goal} ${activityStat.unit}",
                style = TextStyle(color = Color.Gray, fontSize = 14.sp)
            )
        }
    }
}
@Composable
fun getIconForStat(statType: String): Int {
    return when (statType) {
        StatsNames.move -> R.drawable.ic_move
        StatsNames.exercise -> R.drawable.ic_exercise
        StatsNames.stand -> R.drawable.ic_stand
        else -> R.drawable.ic_move  // Default icon if none match
    }
}

fun getColorForStat(statType: String): Color {
    return when (statType) {
        StatsNames.move -> Color.Red
        StatsNames.exercise -> Color.Yellow
        StatsNames.stand -> Color.Blue
        else -> Color.Gray  // Default color if none match
    }
}
