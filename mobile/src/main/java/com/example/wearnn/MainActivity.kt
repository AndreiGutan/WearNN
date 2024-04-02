package com.example.wearnn

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.wearnn.activities.SignInActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check if the user is already signed in
        if (GoogleSignIn.getLastSignedInAccount(this) == null) {
            // Not signed in, redirect to SignInActivity
            val signInIntent = Intent(this, SignInActivity::class.java)
            startActivity(signInIntent)
            finish() // Close MainActivity so the user can't return here with the back button
        }
        // If the user is signed in, continue showing MainActivity
    }
}
