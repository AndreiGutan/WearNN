package com.example.wearnn.presentation.ui.composables.challenges

import android.app.Application
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import com.example.wearnn.R
import com.example.wearnn.utils.AppFonts
import com.example.wearnn.viewModel.ChallengeViewModel
import com.example.wearnn.viewModel.ChallengeViewModelFactory
import com.example.wearnn.viewModel.HealthViewModel

@Composable
fun ChallengeScreen(
    healthViewModel: HealthViewModel,
    challengeViewModel: ChallengeViewModel = viewModel(factory = ChallengeViewModelFactory(LocalContext.current.applicationContext as Application))
) {
    LaunchedEffect(Unit) {
        healthViewModel.setChallengeViewModel(challengeViewModel)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!challengeViewModel.startChallenge) {
                Spacer(modifier = Modifier.weight(1f))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(120.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(
                            color = Color.Gray.copy(alpha = 0.3f),
                        )
                    }
                    Button(
                        onClick = { challengeViewModel.start() },
                        modifier = Modifier.size(120.dp),  // Adjust size as needed
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)  // Make button background transparent
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(80.dp)  // Same size as the Button
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_exercise2),
                                contentDescription = "Start Challenge",
                                modifier = Modifier.size(80.dp)
                            )
                        }
                    }
                }

                Text(
                    text = "Burn 10 calories",
                    fontSize = 16.sp,
                    fontFamily = AppFonts.bebasNeueFont,
                    modifier = Modifier.padding(top = 12.dp)
                )

                Spacer(modifier = Modifier.weight(1f))  // Pushes the button towards the bottom
            } else {
                Spacer(modifier = Modifier.height(20.dp))  // Add a spacer to move the text down
                Text(
                    text = "Kcal: ${challengeViewModel.caloriesBurned}",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontFamily = AppFonts.bebasNeueFont,
                )

                // Centered Animated Heart SVG or Image
                if (challengeViewModel.resumeChallenge) {
                    AnimatedHeart(
                        modifier = Modifier
                            .size(90.dp)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_move),
                        contentDescription = "Heart Image",
                        modifier = Modifier
                            .size(60.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp),  // Added padding to move buttons up
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = {
                        challengeViewModel.reset()
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
}
