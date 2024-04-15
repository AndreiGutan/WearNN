package com.example.wearnn.presentation.ui.composables.statsPerDay

import ActivityStat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.example.wearnn.utils.AppColors
import com.example.wearnn.utils.AppFonts
import com.example.wearnn.viewModel.HealthViewModel


@Composable
fun ActivityRowSimple(activityStat: ActivityStat) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(start = 8.dp)
        ) {
            Text(
                text = activityStat.title,
                color = AppColors.customGreyNonImportant,
                fontFamily = AppFonts.bebasNeueFont,
                style = TextStyle(fontSize = 15.sp)
            )
            Text(
                text = "${activityStat.progress} ${activityStat.unit}",
                color = Color.White,
                fontFamily = AppFonts.bebasNeueFont,
                style = TextStyle(fontSize = 20.sp)
            )
        }
    }
}


@Composable
fun Screen2Week(viewModel: HealthViewModel) {
    // Assume viewModel has a function to get weekly averages for steps, distance, and climbed.
    val weekStats by viewModel.weeklyAverageStats.collectAsState()

    Column(
        modifier = Modifier
            .offset(y = 20.dp)
            .fillMaxSize()
    ) {
        Text(
            text = "This week",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontFamily = AppFonts.bebasNeueFont,
            style = TextStyle(fontSize = 29.sp)
        )
        weekStats.forEach { activityStat ->
            ActivityRowSimple(activityStat)
        }
    }
}
