package com.example.wearnn.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wearnn.R
import com.example.wearnn.utils.PreferencesHelper
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val signUpButton = findViewById<Button>(R.id.signup_button)
        val backButton = findViewById<Button>(R.id.back_to_login_button) // Assuming you've added this button

        signUpButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.signup_email).text.toString().trim()
            val password = findViewById<EditText>(R.id.signup_password).text.toString().trim()

            // Check if an account with this email already exists
            if (PreferencesHelper.getUserEmail(this) == email) {
                Toast.makeText(this, "An account with this email already exists.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (validateInput(email, password)) {
                // Here we store the email and password
                PreferencesHelper.setUserEmail(this, email)
                PreferencesHelper.setUserPassword(this, password)
                // Set the user as logged in
                PreferencesHelper.setUserEmail(this, email)  // Store the email on successful sign up

                // Send account email to Wear OS device
                sendAccountInfoToWear(this, email)

                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            }
        }


        backButton.setOnClickListener {
            // Navigate back to the LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun sendAccountInfoToWear(context: Context, accountEmail: String) {
        val dataClient = Wearable.getDataClient(context)
        val putDataReq = PutDataMapRequest.create("/account_info").apply {
            dataMap.putString("accountEmail", accountEmail)
            // Use a timestamp to ensure the data is treated as new each time
            dataMap.putLong("timestamp", System.currentTimeMillis())
        }.asPutDataRequest()

        Tasks.await(dataClient.putDataItem(putDataReq))
    }
}
