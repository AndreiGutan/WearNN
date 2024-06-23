package com.example.wearnn.presentation.ui.composables.challenges

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.example.wearnn.R
import com.example.wearnn.viewModel.HealthViewModel

@Composable
fun ChallengeScreen(viewModel: HealthViewModel) {
    var startChallenge by remember { mutableStateOf(false) }
    var pauseChallenge by remember { mutableStateOf(false) }
    var resumeChallenge by remember { mutableStateOf(true) }
    var caloriesBurned by remember { mutableStateOf(0) } // This would ideally be updated by some logic observing the activity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!startChallenge && !pauseChallenge) {
            Spacer(modifier = Modifier.weight(1f))
            Text("Burn 10 calories", fontSize = 20.sp)
            Spacer(modifier = Modifier.weight(1f))  // Pushes the button towards the bottom
            Button(onClick = { startChallenge = true }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_exercise),
                    contentDescription = "Start Challenge"
                )
            }
        } else {
            Text("Kcal:", fontSize = 20.sp, color = Color.White)
            Text("$caloriesBurned", fontSize = 24.sp, color = Color.White)
            Spacer(modifier = Modifier.weight(1f))  // Keeps the text aligned at the center after the challenge starts

            // Centered Animated Heart SVG
            if (!pauseChallenge || resumeChallenge) {
                AnimatedHeart(
                    modifier = Modifier
                        .size(65.dp)
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.nn_icon_heart),
                    contentDescription = "Heart Image",
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .size(50.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

            Spacer(modifier = Modifier.height(16.dp)) // Space between SVG and buttons

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!pauseChallenge) {
                    Button(onClick = {
                        pauseChallenge = true
                        resumeChallenge = false
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.pause_icon),
                            contentDescription = "pause Challenge"
                        )
                    }
                }
                if (!resumeChallenge) {
                    Button(onClick = {
                        resumeChallenge = true
                        pauseChallenge = false
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.play_icon),
                            contentDescription = "resume Challenge"
                        )
                    }
                }

                Button(onClick = {
                    startChallenge = false
                    pauseChallenge = false
                    resumeChallenge = true
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.arrowleft_24px),
                        contentDescription = "Restart Challenge"
                    )
                }
            }

        }
    }
}