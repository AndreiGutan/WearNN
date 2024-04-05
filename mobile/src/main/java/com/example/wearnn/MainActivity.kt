package com.example.wearnn

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.wearnn.activities.DashboardActivity
import com.example.wearnn.activities.LoginActivity
import com.example.wearnn.utils.PreferencesHelper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if user is logged in
        if (PreferencesHelper.isLoggedIn(this)) {
            // User is logged in, proceed to dashboard
            val dashboardIntent = Intent(this, DashboardActivity::class.java)
            startActivity(dashboardIntent)
        } else {
            // User is not logged in, redirect to login screen
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
        finish() // Close MainActivity
    }
}
