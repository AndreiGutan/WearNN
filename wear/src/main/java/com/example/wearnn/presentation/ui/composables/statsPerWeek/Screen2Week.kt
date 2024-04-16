package com.example.wearnn.presentation.ui.composables.statsPerWeek

import ActivityStat
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.example.wearnn.R
import com.example.wearnn.utils.AppColors
import com.example.wearnn.utils.AppFonts
import com.example.wearnn.utils.StatsNames
import com.example.wearnn.utils.Units
import com.example.wearnn.viewModel.HealthViewModel

@Composable
fun ActivityRowWeeklyAvg(activityStat: ActivityStat) {
    val iconId = when (activityStat.title) {
        StatsNames.avgsteps, StatsNames.steps -> R.drawable.ic_steps
        StatsNames.avgdistance, StatsNames.distance -> R.drawable.ic_distance
        StatsNames.avgclimbed, StatsNames.climbed -> R.drawable.ic_climb
        else -> null
    }
    if (iconId == null) {
        println("Debug: Icon ID is null for ${activityStat.title}")
    } else {
        println("Debug: Loading icon for ${activityStat.title}")
    }



    val displayText = if (activityStat.unit == Units.kilometers) {
        val kilometers = activityStat.progress / 1000.0  // Convert meters to kilometers
        "${String.format("%.2f", kilometers)} km"  // Format to two decimal places
    } else {
        "${activityStat.progress} ${activityStat.unit}"
    }

    Row(
        modifier = Modifier
            .offset(x = 4.dp)
            .fillMaxWidth()
            .padding(horizontal = 50.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        iconId?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = "${activityStat.title} icon",
                modifier = Modifier.size(27.dp)
            )
        }
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(
                text = activityStat.title,
                style = TextStyle(fontSize = 15.sp),
                color = AppColors.customGreyNonImportant,
                fontFamily = AppFonts.bebasNeueFont
            )
            Text(
                text = displayText,
                style = TextStyle(fontSize = 20.sp),
                fontFamily = AppFonts.bebasNeueFont
            )
        }
    }
}

@Composable
fun Screen2Week(viewModel: HealthViewModel) {
    val weekStats by viewModel.weeklyAverageStats.collectAsState()

    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "This Week",
            fontFamily = AppFonts.bebasNeueFont,
            style = TextStyle(fontSize = 29.sp)
        )
        weekStats.forEach { activityStat ->
            ActivityRowWeeklyAvg(activityStat)
        }
    }
}
