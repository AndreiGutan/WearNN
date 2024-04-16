import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.example.wearnn.R
import com.example.wearnn.utils.AppFonts

@Composable
fun Screen3Week() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 25.dp), // Reduce top padding to move content up
        contentAlignment = Alignment.TopCenter // Aligns children at the top center of the box
    ) {
        Image(

            painter = painterResource(id = R.drawable.logo_nn_mare),
            contentDescription = "NN Logo",
            modifier = Modifier.size(350.dp),
            alignment = Alignment.TopCenter

        )
        Text(
            text = "You NN everywhere",
            fontFamily = AppFonts.bebasNeueFont,
            fontSize = 20.sp,
            color = Color.White, // Ensures text is visible on top of any logo color
            fontWeight = FontWeight.Bold, // Enhances text legibility
            textAlign = TextAlign.Center,
            modifier = Modifier
                .offset(y = 130.dp) // Position text further down from the image
        )
    }
}
