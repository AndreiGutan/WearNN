package com.example.wearnn.presentation.ui.composables.statsPerDay

import ActivityStat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.example.wearnn.utils.AppFonts
import com.example.wearnn.viewModel.HealthViewModel

@Composable
fun ActivityRow3Day(activityStat: ActivityStat) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(
                text = activityStat.title,
                style = TextStyle(fontSize = 15.sp),
                fontFamily = AppFonts.bebasNeueFont
            )
            Text(
                text = "${activityStat.progress} ${activityStat.unit}",
                style = TextStyle(fontSize = 20.sp),
                fontFamily = AppFonts.bebasNeueFont
            )
        }
    }
}

@Composable
fun Screen3Day(viewModel: HealthViewModel) {
    val activities by viewModel.configuredDailyData.collectAsState()  // This should now include steps, distance, climbed

    Column(
        modifier = Modifier.padding(top = 20.dp)
    ) {
        Text(
            text = "Today's Activity",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontFamily = AppFonts.bebasNeueFont,
            style = TextStyle(fontSize = 29.sp)
        )
        activities.filter {
            it.title in listOf("Steps", "Distance", "Climbed")
        }.forEach { activityStat ->
            ActivityRow3Day(activityStat)
        }
    }
}
