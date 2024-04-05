package com.example.wearnn.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.wearnn.R

class YourConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout for your confirmation activity
        setContentView(R.layout.activity_your_confirmation)

        // Retrieve the account email passed through the intent
        val accountEmail = intent.getStringExtra("accountEmail")

        // Set up the UI to display the account email
        val emailTextView = findViewById<TextView>(R.id.emailTextView)
        emailTextView.text = accountEmail

        // Handle the confirmation button click
        val confirmButton = findViewById<Button>(R.id.confirmButton)
        confirmButton.setOnClickListener {
            // Handle account confirmation logic here
            // For example, save the account information in SharedPreferences or start syncing data

            // After handling the confirmation, you can close this activity
            finish()
        }
    }
}
