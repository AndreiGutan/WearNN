package com.example.wearnn.presentation.ui.composables.statsPerDay

import ActivityStat
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import com.example.wearnn.R

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.example.wearnn.utils.AppColors
import com.example.wearnn.utils.AppFonts
import com.example.wearnn.utils.StatsNames
import com.example.wearnn.viewModel.HealthViewModel

@Composable
fun ActivityRow2Day(activityStat: ActivityStat) {
    val iconId = when (activityStat.title) {
        StatsNames.move -> R.drawable.ic_move
        StatsNames.exercise -> R.drawable.ic_exercise
        StatsNames.stand -> R.drawable.ic_stand
        else -> null
    }

    Row(
        modifier = Modifier
            .offset(x = 10.dp)
            .fillMaxWidth()
            .padding(horizontal = 55.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.size(38.dp),
            contentAlignment = Alignment.Center
        ) {
            // Draw the arc
            Canvas(modifier = Modifier.matchParentSize()) {
                activityStat.color?.let {
                    drawArc(
                        color = it.copy(alpha = 0.3f),
                        startAngle = -225f,
                        sweepAngle = 270f,
                        useCenter = false,
                        style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
                    )
                    activityStat.angle?.let { angle ->
                        drawArc(
                            color = it,
                            startAngle = -225f,
                            sweepAngle = angle,
                            useCenter = false,
                            style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                }
            }

            // Overlay the icon
            iconId?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = "${activityStat.title} icon",
                    modifier = Modifier.size(30.dp)  // Smaller size to fit within the arc
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(
                text = activityStat.title,
                color = AppColors.customGreyNonImportant,
                fontFamily = AppFonts.bebasNeueFont,
                style = TextStyle(fontSize = 15.sp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                activityStat.color?.let {
                    Text(
                        text = "${activityStat.progress}",
                        color = it,
                        fontFamily = AppFonts.bebasNeueFont,
                        style = TextStyle(fontSize = 20.sp)
                    )
                }
                Text(
                    text = "/${activityStat.goal}",
                    color = Color.White,
                    fontFamily = AppFonts.bebasNeueFont,
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.offset(y = 1.7.dp)
                )
                Text(
                    text = " ${activityStat.unit}",
                    color = Color.White,
                    fontFamily = AppFonts.bebasNeueFont,
                    style = TextStyle(fontSize = 10.sp),
                    modifier = Modifier.offset(y = 2.8.dp)
                )
            }
        }
    }
}


@Composable
fun Screen2Day(viewModel: HealthViewModel) {
    val activities by viewModel.configuredDailyData.collectAsState()

    // Define the order explicitly
    val desiredOrder = listOf(StatsNames.move, StatsNames.exercise, StatsNames.stand)

    // Filter and sort activities based on the desired order
    val filteredActivities = activities.filter {
        it.title in desiredOrder
    }.sortedBy {
        desiredOrder.indexOf(it.title) // This sorts the activities based on the order defined in desiredOrder
    }

    Column(
        modifier = Modifier
            .offset(y = 20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Today",
            fontFamily = AppFonts.bebasNeueFont,
            style = TextStyle(fontSize = 29.sp)
        )
        filteredActivities.forEach { activityStat ->
            ActivityRow2Day(activityStat)
        }
    }
}
