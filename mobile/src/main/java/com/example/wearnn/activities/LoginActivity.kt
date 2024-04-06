package com.example.wearnn.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wearnn.R
import com.example.wearnn.utils.PreferencesHelper
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (PreferencesHelper.isLoggedIn(this)) {
            navigateToDashboard()
        }

        findViewById<Button>(R.id.button_login).setOnClickListener {
            val email = findViewById<EditText>(R.id.login_email).text.toString().trim()
            val password = findViewById<EditText>(R.id.login_password).text.toString().trim()

            if (validateInput(email, password)) {
                authenticateUser(email, password)
            }
        }

        findViewById<Button>(R.id.goto_signup_button).setOnClickListener {
            val signUpIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signUpIntent)
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        return when {
            email.isEmpty() || password.isEmpty() -> {
                Toast.makeText(this, "Email and password must not be empty.", Toast.LENGTH_SHORT).show()
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show()
                false
            }
            password.length < 6 -> {
                Toast.makeText(this, "Password must be at least 6 characters long.", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun authenticateUser(email: String, password: String) {
        val storedEmail = PreferencesHelper.getUserEmail(this)
        val storedPassword = PreferencesHelper.getUserPassword(this)

        if (email == storedEmail && password == storedPassword) {
            PreferencesHelper.setLoggedIn(this, true)
            PreferencesHelper.setUserEmail(this, email)
            Log.d("LoginDebug", "Login successful")
            navigateToDashboard()
        } else {
            Log.d("LoginDebug", "Login failed")
            Toast.makeText(this, "Email or password is incorrect.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}
