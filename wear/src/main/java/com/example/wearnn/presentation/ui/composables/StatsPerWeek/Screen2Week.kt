package com.example.wearnn.presentation.ui.composables.StatsPerDay

import ActivityStat
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.wearnn.R
import com.example.wearnn.data.model.HealthStats
import com.example.wearnn.utils.AppColors
import com.example.wearnn.utils.AppFonts


// this is just a demo to show how you can inflate xml's into Composable
@Composable
fun ActivityRow2Week(activityStat: ActivityStat, arcColor: Color, greyColor: Color, whiteColor: Color) {
    Row(
        modifier = Modifier
            .offset(x=45.dp)
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Canvas(modifier = Modifier.size(35.dp)) { // Adjust the size as needed
            drawArc(
                color = arcColor.copy(alpha = 0.3f), // Draw the "empty" arc in the background with less alpha
                startAngle = -225f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = arcColor, // Draw the "filled" arc representing the progress
                startAngle = -225f,
                sweepAngle = activityStat.angle,
                useCenter = false,
                style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        Column(

            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .offset(x=9.dp)
                .padding(start = 8.dp)
        ) {
            Text(
                text = activityStat.title,
                color = greyColor,
                fontFamily = AppFonts.bebasNeueFont,
                style = TextStyle(fontSize = 15.sp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${activityStat.progress}",
                    color = arcColor,
                    fontFamily = AppFonts.bebasNeueFont,
                    style = TextStyle(fontSize = 20.sp)
                )
                Text(

                    text = " ${activityStat.unit}",
                    color = whiteColor,
                    fontFamily = AppFonts.bebasNeueFont,
                    style = TextStyle(fontSize = 10.sp),
                    modifier = Modifier
                        .offset(y = 2.8.dp)
                )
            }
        }
    }
}



@Composable
fun Screen2Week(activities: List<ActivityStat>) {
    Column(
        modifier = Modifier
            .offset(y = 20.dp)
            .fillMaxSize()
    ) {
        Text(
            text = "This week",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontFamily = AppFonts.bebasNeueFont,
            style = TextStyle(fontSize = 29.sp) // Specify other text styles if needed
        )
        activities.forEach { activityStat ->
            ActivityRow2Week(
                activityStat = activityStat,
                arcColor = activityStat.color, // This would be the color corresponding to the arc
                greyColor = AppColors.customGreyNonImportant, // Replace with your actual color
                whiteColor = Color.White
            )
        }
    }
}

