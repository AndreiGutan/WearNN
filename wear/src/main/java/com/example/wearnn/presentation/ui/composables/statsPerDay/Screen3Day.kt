package com.example.wearnn.presentation.ui.composables.statsPerDay

import ActivityStat
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun ActivityRow3Day(activityStat: ActivityStat) {
    val iconId = when (activityStat.title) {
        StatsNames.steps -> R.drawable.ic_steps
        StatsNames.distance -> R.drawable.ic_distance
        StatsNames.climbed -> R.drawable.ic_climb
        else -> null
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
fun Screen3Day(viewModel: HealthViewModel) {
    val activities by viewModel.configuredDailyData.collectAsState()

    // Filter for specific activities
    val filteredActivities = activities.filter {
        it.title in listOf(StatsNames.steps, StatsNames.distance, StatsNames.climbed)
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
            ActivityRow3Day(activityStat)
        }
    }
}
