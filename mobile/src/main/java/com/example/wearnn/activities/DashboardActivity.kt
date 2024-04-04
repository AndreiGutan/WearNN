package com.example.wearnn.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.wearnn.R
import com.example.wearnn.utils.PreferencesHelper

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val userEmailTextView: TextView = findViewById(R.id.userEmailTextView)
        val logoutButton: Button = findViewById(R.id.logoutButton)

        // Retrieve and display the user's email from Preferences (or intent extras)
        userEmailTextView.text = PreferencesHelper.getUserEmail(applicationContext)

        logoutButton.setOnClickListener {
            // Only set the user as logged out. Do not clear the email or password.
            PreferencesHelper.setLoggedIn(applicationContext, false)

            // Redirect to LoginActivity
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish() // Ensure DashboardActivity is closed
        }

    }
}
