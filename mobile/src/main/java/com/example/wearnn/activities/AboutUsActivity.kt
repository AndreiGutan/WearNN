package com.example.wearnn.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.wearnn.R

class AboutUsActivity : DashboardActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_us)

        //adds navigation to main Dashboard activity when clicking the NN Logo
        val nnLogo: ImageView = findViewById(R.id.NNMobileHome)
        nnLogo.setOnClickListener{
            startActivity(Intent(this, DashboardActivity::class.java))
        }
    }
}